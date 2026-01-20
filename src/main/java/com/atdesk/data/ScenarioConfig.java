package com.atdesk.data;

import java.util.List;

public class ScenarioConfig {
    private List<AircraftConfig> aircraft;
    private int spawnIntervalTicks;
    private int maxActiveAircraft;
    private List<SpawnTemplate> spawnTemplates;

    public List<AircraftConfig> getAircraft() {
        return aircraft;
    }

    public int getSpawnIntervalTicks() {
        return spawnIntervalTicks;
    }

    public int getMaxActiveAircraft() {
        return maxActiveAircraft;
    }

    public List<SpawnTemplate> getSpawnTemplates() {
        return spawnTemplates;
    }
}
