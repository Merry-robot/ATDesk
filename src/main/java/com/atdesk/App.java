package com.atdesk;

import com.atdesk.core.Aircraft;
import com.atdesk.core.AircraftState;
import com.atdesk.core.Position;
import com.atdesk.core.Runway;
import com.atdesk.core.SimulationLoop;
import com.atdesk.core.SimulationState;
import com.atdesk.data.AircraftConfig;
import com.atdesk.data.AirportConfig;
import com.atdesk.data.JsonLoader;
import com.atdesk.data.ScenarioConfig;
import com.atdesk.input.CommandPanel;
import com.atdesk.input.CommandProcessor;
import com.atdesk.rendering.ScopePanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        JsonLoader loader = new JsonLoader();
        AirportConfig airportConfig = loader.loadAirport("/data/airport.json");
        ScenarioConfig scenarioConfig = loader.loadScenario("/data/scenario.json");

        Runway runway = new Runway(airportConfig.getRunwayName(), airportConfig.getRunwayLength());
        List<Aircraft> aircraft = new ArrayList<>();
        for (AircraftConfig config : scenarioConfig.getAircraft()) {
            AircraftState state = AircraftState.valueOf(config.getState());
            aircraft.add(new Aircraft(
                config.getId(),
                config.getCallsign(),
                new Position(config.getX(), config.getY()),
                state,
                config.getRunway()
            ));
        }

        SimulationState simulationState = new SimulationState(airportConfig, runway, aircraft);
        SimulationLoop loop = new SimulationLoop(simulationState, 1000);

        SwingUtilities.invokeLater(() -> buildUi(simulationState, loop));
    }

    private static void buildUi(SimulationState simulationState, SimulationLoop loop) {
        JFrame frame = new JFrame("ATDesk - Tower Scope");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // UI composition keeps input, rendering, and simulation state separated.
        ScopePanel scopePanel = new ScopePanel(simulationState);
        CommandProcessor processor = new CommandProcessor(simulationState);
        CommandPanel commandPanel = new CommandPanel(processor);

        JPanel content = new JPanel(new BorderLayout());
        content.add(scopePanel, BorderLayout.CENTER);
        content.add(commandPanel, BorderLayout.SOUTH);
        frame.setContentPane(content);

        Timer repaintTimer = new Timer(100, event -> scopePanel.repaint());
        repaintTimer.start();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loop.start();
    }
}
