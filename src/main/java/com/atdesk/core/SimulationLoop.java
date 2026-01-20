package com.atdesk.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulationLoop {
    private final SimulationState state;
    private final ScheduledExecutorService scheduler;
    private final long tickMillis;

    public SimulationLoop(SimulationState state, long tickMillis) {
        this.state = state;
        this.tickMillis = tickMillis;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Fixed-tick loop: the core simulation advances at a deterministic cadence.
     * Rendering runs independently to keep the simulation rule-driven.
     */
    public void start() {
        scheduler.scheduleAtFixedRate(state::tick, 0, tickMillis, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
    }
}
