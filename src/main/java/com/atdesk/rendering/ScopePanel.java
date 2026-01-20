package com.atdesk.rendering;

import com.atdesk.core.Aircraft;
import com.atdesk.core.Position;
import com.atdesk.core.SimulationState;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ScopePanel extends JPanel {
    private final SimulationState simulationState;

    public ScopePanel(SimulationState simulationState) {
        this.simulationState = simulationState;
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(10, 20, 10));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // Rendering only: no simulation logic here.
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double scale = calculateScale();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        drawPattern(g2, centerX, centerY, scale);
        drawRunway(g2, centerX, centerY, scale);
        drawAircraft(g2, centerX, centerY, scale);
        drawOverlay(g2);
    }

    private double calculateScale() {
        double extent = simulationState.getAirportConfig().getPatternLength();
        double runway = simulationState.getAirportConfig().getRunwayLength();
        double span = Math.max(extent * 2.0, runway * 1.5);
        return Math.min(getWidth(), getHeight()) / span;
    }

    private void drawPattern(Graphics2D g2, int centerX, int centerY, double scale) {
        double offset = simulationState.getAirportConfig().getPatternOffset();
        double length = simulationState.getAirportConfig().getPatternLength();
        int x = centerX + (int) (offset * scale);
        int y = centerY - (int) (length * scale);
        int width = (int) (offset * scale);
        int height = (int) (length * 2 * scale);
        g2.setColor(new Color(60, 90, 60));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRect(x, y, width, height);
    }

    private void drawRunway(Graphics2D g2, int centerX, int centerY, double scale) {
        double runwayLength = simulationState.getAirportConfig().getRunwayLength();
        int halfLength = (int) (runwayLength / 2.0 * scale);
        int width = (int) (20 * scale);
        g2.setColor(new Color(120, 120, 120));
        g2.fillRect(centerX - width / 2, centerY - halfLength, width, halfLength * 2);
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(centerX - width / 2, centerY - halfLength, width, halfLength * 2);
    }

    private void drawAircraft(Graphics2D g2, int centerX, int centerY, double scale) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (Aircraft aircraft : simulationState.getAircraft()) {
            Position pos = aircraft.getPosition();
            int x = centerX + (int) (pos.getX() * scale);
            int y = centerY - (int) (pos.getY() * scale);
            g2.setColor(new Color(0, 220, 120));
            g2.fillOval(x - 4, y - 4, 8, 8);
            g2.setColor(Color.WHITE);
            g2.drawString(aircraft.getCallsign() + " " + aircraft.getState(), x + 6, y - 6);
        }
    }

    private void drawOverlay(Graphics2D g2) {
        g2.setColor(new Color(120, 160, 120));
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        String title = simulationState.getAirportConfig().getAirportCode()
            + " RWY " + simulationState.getAirportConfig().getRunwayName()
            + " | Ticks: " + simulationState.getTickCount()
            + " | Runway: " + (simulationState.getRunway().isOccupied() ? "OCCUPIED" : "CLEAR");
        g2.drawString(title, 12, 20);
    }
}
