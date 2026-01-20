package com.atdesk.core;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Position other) {
        double dx = other.x - x;
        double dy = other.y - y;
        return Math.hypot(dx, dy);
    }

    public void moveToward(Position target, double distance) {
        double dx = target.x - x;
        double dy = target.y - y;
        double length = Math.hypot(dx, dy);
        if (length == 0) {
            return;
        }
        double ratio = Math.min(1.0, distance / length);
        x += dx * ratio;
        y += dy * ratio;
    }
}
