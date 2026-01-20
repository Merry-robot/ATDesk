package com.atdesk.core;

import java.util.List;

public class TrafficGenerator {
    private final List<TrafficTemplate> templates;
    private final int intervalTicks;
    private final int maxActive;
    private int templateIndex;
    private int sequence;

    public TrafficGenerator(List<TrafficTemplate> templates, int intervalTicks, int maxActive) {
        this.templates = templates;
        this.intervalTicks = intervalTicks;
        this.maxActive = maxActive;
    }

    public void onTick(long tickCount, SimulationState simulationState) {
        if (templates.isEmpty() || intervalTicks <= 0 || maxActive <= 0) {
            return;
        }
        if (tickCount % intervalTicks != 0) {
            return;
        }
        if (simulationState.getAircraft().size() >= maxActive) {
            return;
        }
        TrafficTemplate template = templates.get(templateIndex);
        templateIndex = (templateIndex + 1) % templates.size();
        sequence++;
        String id = template.getIdPrefix() + sequence;
        String callsign = template.getCallsignPrefix() + (100 + sequence);
        Aircraft newAircraft = new Aircraft(
            id,
            callsign,
            new Position(template.getInitialPosition().getX(), template.getInitialPosition().getY()),
            template.getInitialState(),
            template.getRunway()
        );
        simulationState.addAircraft(newAircraft);
    }
}
