package com.atdesk.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonLoader {
    private final ObjectMapper mapper = new ObjectMapper();

    public AirportConfig loadAirport(String resourcePath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Missing resource: " + resourcePath);
            }
            return mapper.readValue(inputStream, AirportConfig.class);
        }
    }

    public ScenarioConfig loadScenario(String resourcePath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Missing resource: " + resourcePath);
            }
            return mapper.readValue(inputStream, ScenarioConfig.class);
        }
    }
}
