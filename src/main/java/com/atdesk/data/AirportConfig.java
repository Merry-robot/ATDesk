package com.atdesk.data;

import com.atdesk.core.Position;

public class AirportConfig {
    private String airportCode;
    private String runwayName;
    private double runwayLength;
    private double patternOffset;
    private double patternLength;
    private double taxiSpeed;
    private double rolloutSpeed;
    private double patternSpeed;

    public String getAirportCode() {
        return airportCode;
    }

    public String getRunwayName() {
        return runwayName;
    }

    public double getRunwayLength() {
        return runwayLength;
    }

    public double getPatternOffset() {
        return patternOffset;
    }

    public double getPatternLength() {
        return patternLength;
    }

    public double getTaxiSpeed() {
        return taxiSpeed;
    }

    public double getRolloutSpeed() {
        return rolloutSpeed;
    }

    public double getPatternSpeed() {
        return patternSpeed;
    }

    public Position getRunwayThreshold() {
        return new Position(0, -runwayLength / 2.0);
    }

    public Position getUpwindEnd() {
        return new Position(0, runwayLength / 2.0);
    }

    public Position getHoldShortPoint() {
        return new Position(-patternOffset / 2.0, -runwayLength / 2.0 - 1.0);
    }

    public Position getCrosswindTurnPoint() {
        return new Position(0, runwayLength / 2.0 + patternLength * 0.2);
    }

    public Position getDownwindEntry() {
        return new Position(patternOffset, runwayLength / 2.0 + patternLength * 0.2);
    }

    public Position getBaseTurnPoint() {
        return new Position(patternOffset, -runwayLength / 2.0 - patternLength * 0.2);
    }

    public Position getFinalApproachPoint() {
        return new Position(0, -runwayLength / 2.0 - patternLength * 0.2);
    }

    public Position getRunwayExitPoint() {
        return new Position(-patternOffset / 2.0, 0);
    }

    public Position getRampPoint() {
        return new Position(-patternOffset, -runwayLength / 2.0 - patternLength * 0.4);
    }
}
