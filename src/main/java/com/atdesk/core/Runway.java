package com.atdesk.core;

public class Runway {
    private final String name;
    private final double length;
    private String occupiedBy;

    public Runway(String name, double length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public boolean isOccupied() {
        return occupiedBy != null;
    }

    public String getOccupiedBy() {
        return occupiedBy;
    }

    public boolean requestOccupancy(String aircraftId) {
        if (occupiedBy == null || occupiedBy.equals(aircraftId)) {
            occupiedBy = aircraftId;
            return true;
        }
        return false;
    }

    public void release(String aircraftId) {
        if (occupiedBy != null && occupiedBy.equals(aircraftId)) {
            occupiedBy = null;
        }
    }
}
