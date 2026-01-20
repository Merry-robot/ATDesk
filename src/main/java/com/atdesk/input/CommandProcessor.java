package com.atdesk.input;

import com.atdesk.core.Aircraft;
import com.atdesk.core.AircraftState;
import com.atdesk.core.SimulationState;

import java.util.Locale;
import java.util.Optional;

public class CommandProcessor {
    private final SimulationState simulationState;

    public CommandProcessor(SimulationState simulationState) {
        this.simulationState = simulationState;
    }

    public String handleCommand(String commandLine) {
        if (commandLine == null || commandLine.isBlank()) {
            return "Empty command.";
        }
        String[] tokens = commandLine.trim().split("\\s+");
        if (tokens.length < 2) {
            return "Format: <CALLSIGN> <COMMAND> [ARG]";
        }
        String target = tokens[0].toUpperCase(Locale.ROOT);
        Optional<Aircraft> aircraftOpt = simulationState.findAircraft(target);
        if (aircraftOpt.isEmpty()) {
            return "Unknown aircraft: " + target;
        }
        Aircraft aircraft = aircraftOpt.get();
        String verb = tokens[1].toUpperCase(Locale.ROOT);
        AircraftState desiredState = switch (verb) {
            case "TAXI" -> AircraftState.TAXI_OUT;
            case "HOLD" -> AircraftState.HOLD_SHORT;
            case "LINEUP" -> AircraftState.LINE_UP;
            case "TAKEOFF" -> AircraftState.TAKEOFF;
            case "LAND" -> AircraftState.FINAL;
            case "EXIT" -> AircraftState.TAXI_IN;
            case "TURN" -> parseTurnCommand(tokens);
            default -> null;
        };
        if (desiredState == null) {
            return "Unknown command: " + verb;
        }
        boolean accepted = simulationState.requestStateChange(aircraft, desiredState);
        return accepted
            ? "ACK " + aircraft.getCallsign() + " " + desiredState
            : "UNABLE " + aircraft.getCallsign() + " " + desiredState;
    }

    private AircraftState parseTurnCommand(String[] tokens) {
        if (tokens.length < 3) {
            return null;
        }
        return switch (tokens[2].toUpperCase(Locale.ROOT)) {
            case "CROSSWIND" -> AircraftState.CROSSWIND;
            case "DOWNWIND" -> AircraftState.DOWNWIND;
            case "BASE" -> AircraftState.BASE;
            case "FINAL" -> AircraftState.FINAL;
            default -> null;
        };
    }
}
