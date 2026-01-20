package com.atdesk.core;

public class TrafficTemplate {
    private final String idPrefix;
    private final String callsignPrefix;
    private final AircraftState initialState;
    private final Position initialPosition;
    private final String runway;

    public TrafficTemplate(String idPrefix, String callsignPrefix, AircraftState initialState, Position initialPosition, String runway) {
        this.idPrefix = idPrefix;
        this.callsignPrefix = callsignPrefix;
        this.initialState = initialState;
        this.initialPosition = initialPosition;
        this.runway = runway;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public String getCallsignPrefix() {
        return callsignPrefix;
    }

    public AircraftState getInitialState() {
        return initialState;
    }

    public Position getInitialPosition() {
        return initialPosition;
    }

    public String getRunway() {
        return runway;
    }
}
