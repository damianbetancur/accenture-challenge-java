package com.accenture.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class OrdersTestHelper {
    private static final String TEST_DATA_FILE = "/orders.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, Map<String, Object>> testData;

    static {
        try (InputStream in = OrdersTestHelper.class.getResourceAsStream(TEST_DATA_FILE)) {
            if (in == null) {
                throw new IllegalArgumentException("Test data file not found: " + TEST_DATA_FILE);
            }
            testData = objectMapper.readValue(in, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data from " + TEST_DATA_FILE, e);
        }
    }

    public static Map<String, Object> getNormalOrderJsonMap() {
        return testData.getOrDefault("normalOrderJsonMap", Map.of());
    }

    public static Map<String, Object> getInvalidOrderEmptyItemsJsonMap() {
        return testData.getOrDefault("invalidOrderEmptyItemsJsonMap", Map.of());
    }

    public static Map<String, Object> getInvalidIdOrderEmptyJsonMap() {
        return testData.getOrDefault("invalidIdOrderEmptyJsonMap", Map.of());
    }

}
