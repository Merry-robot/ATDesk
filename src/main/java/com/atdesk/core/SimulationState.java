package com.atdesk.core;

import com.atdesk.data.AirportConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SimulationState {
    private final AirportConfig airportConfig;
    private final Runway runway;
    private final List<Aircraft> aircraft;
    private TrafficGenerator trafficGenerator;
    private final double arrivalThreshold = 0.5;
    private long tickCount;

    public SimulationState(AirportConfig airportConfig, Runway runway, List<Aircraft> aircraft) {
        this.airportConfig = airportConfig;
        this.runway = runway;
        this.aircraft = new ArrayList<>(aircraft);
    }

    public SimulationState(AirportConfig airportConfig, Runway runway, List<Aircraft> aircraft, TrafficGenerator trafficGenerator) {
        this(airportConfig, runway, aircraft);
        this.trafficGenerator = trafficGenerator;
    }

    public AirportConfig getAirportConfig() {
        return airportConfig;
    }

    public Runway getRunway() {
        return runway;
    }

    public List<Aircraft> getAircraft() {
        return Collections.unmodifiableList(aircraft);
    }

    public void addAircraft(Aircraft newAircraft) {
        aircraft.add(newAircraft);
    }

    public long getTickCount() {
        return tickCount;
    }

    public double getArrivalThreshold() {
        return arrivalThreshold;
    }

    public void tick() {
        tickCount++;
        for (Aircraft aircraft : aircraft) {
            aircraft.update(this);
        }
        if (trafficGenerator != null) {
            trafficGenerator.onTick(tickCount, this);
        }
    }

    public Optional<Aircraft> findAircraft(String idOrCallsign) {
        return aircraft.stream()
            .filter(candidate -> candidate.matchesId(idOrCallsign))
            .findFirst();
    }

    public double getSpeedForState(AircraftState state) {
        // Deterministic, rule-driven speeds per state (no physics).
        return switch (state) {
            case TAXI_OUT, TAXI_IN -> airportConfig.getTaxiSpeed();
            case TAKEOFF, UPWIND, CROSSWIND, DOWNWIND, BASE, FINAL -> airportConfig.getPatternSpeed();
            case LINE_UP, LANDED -> airportConfig.getRolloutSpeed();
            default -> 0.0;
        };
    }

    public Position getTargetForState(Aircraft aircraft) {
        return switch (aircraft.getState()) {
            case TAXI_OUT -> airportConfig.getHoldShortPoint();
            case LINE_UP -> airportConfig.getRunwayThreshold();
            case TAKEOFF -> airportConfig.getUpwindEnd();
            case UPWIND -> airportConfig.getCrosswindTurnPoint();
            case CROSSWIND -> airportConfig.getDownwindEntry();
            case DOWNWIND -> airportConfig.getBaseTurnPoint();
            case BASE -> airportConfig.getFinalApproachPoint();
            case FINAL -> airportConfig.getRunwayThreshold();
            case LANDED -> airportConfig.getRunwayExitPoint();
            case TAXI_IN -> airportConfig.getRampPoint();
            default -> null;
        };
    }

    public void advanceState(Aircraft aircraft) {
        // State transitions are explicit and deterministic.
        AircraftState current = aircraft.getState();
        AircraftState next = switch (current) {
            case TAXI_OUT -> AircraftState.HOLD_SHORT;
            case LINE_UP -> AircraftState.TAKEOFF;
            case TAKEOFF -> AircraftState.UPWIND;
            case UPWIND -> AircraftState.CROSSWIND;
            case CROSSWIND -> AircraftState.DOWNWIND;
            case DOWNWIND -> AircraftState.BASE;
            case BASE -> AircraftState.FINAL;
            case FINAL -> AircraftState.LANDED;
            case LANDED -> AircraftState.TAXI_IN;
            case TAXI_IN -> AircraftState.PARKED;
            default -> current;
        };
        applyStateChange(aircraft, next);
    }

    public boolean requestStateChange(Aircraft aircraft, AircraftState nextState) {
        if (!isTransitionAllowed(aircraft.getState(), nextState)) {
            return false;
        }
        // Runway occupancy is enforced at the state-machine boundary.
        if ((nextState == AircraftState.LINE_UP || nextState == AircraftState.FINAL)
            && !runway.requestOccupancy(aircraft.getId())) {
            return false;
        }
        applyStateChange(aircraft, nextState);
        return true;
    }

    private void applyStateChange(Aircraft aircraft, AircraftState nextState) {
        AircraftState previous = aircraft.getState();
        if (previous == AircraftState.LANDED || previous == AircraftState.LINE_UP || previous == AircraftState.FINAL) {
            if (nextState == AircraftState.TAXI_IN || nextState == AircraftState.UPWIND) {
                runway.release(aircraft.getId());
            }
        }
        if (nextState == AircraftState.LINE_UP || nextState == AircraftState.FINAL) {
            runway.requestOccupancy(aircraft.getId());
        }
        aircraft.setState(nextState);
    }

    private boolean isTransitionAllowed(AircraftState current, AircraftState next) {
        return switch (current) {
            case PARKED -> next == AircraftState.TAXI_OUT;
            case TAXI_OUT -> next == AircraftState.HOLD_SHORT;
            case HOLD_SHORT -> next == AircraftState.LINE_UP;
            case LINE_UP -> next == AircraftState.TAKEOFF;
            case TAKEOFF -> next == AircraftState.UPWIND;
            case UPWIND -> next == AircraftState.CROSSWIND;
            case CROSSWIND -> next == AircraftState.DOWNWIND;
            case DOWNWIND -> next == AircraftState.BASE;
            case BASE -> next == AircraftState.FINAL;
            case FINAL -> next == AircraftState.LANDED;
            case LANDED -> next == AircraftState.TAXI_IN;
            case TAXI_IN -> next == AircraftState.PARKED;
            default -> false;
        };
    }
}
