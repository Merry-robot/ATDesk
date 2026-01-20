package com.atdesk.core;

import java.util.Objects;

public class Aircraft {
    private final String id;
    private final String callsign;
    private final Position position;
    private AircraftState state;
    private final String assignedRunway;

    public Aircraft(String id, String callsign, Position position, AircraftState state, String assignedRunway) {
        this.id = id;
        this.callsign = callsign;
        this.position = position;
        this.state = state;
        this.assignedRunway = assignedRunway;
    }

    public String getId() {
        return id;
    }

    public String getCallsign() {
        return callsign;
    }

    public Position getPosition() {
        return position;
    }

    public AircraftState getState() {
        return state;
    }

    public void setState(AircraftState state) {
        this.state = state;
    }

    public String getAssignedRunway() {
        return assignedRunway;
    }

    public void update(SimulationState simState) {
        // Each aircraft advances via state-driven target points, not physics.
        Position target = simState.getTargetForState(this);
        if (target == null) {
            return;
        }
        double speed = simState.getSpeedForState(state);
        position.moveToward(target, speed);

        if (position.distanceTo(target) <= simState.getArrivalThreshold()) {
            simState.advanceState(this);
        }
    }

    public boolean matchesId(String lookup) {
        return Objects.equals(id, lookup) || Objects.equals(callsign, lookup);
    }
}
