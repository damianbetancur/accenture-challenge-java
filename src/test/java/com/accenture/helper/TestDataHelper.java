package com.accenture.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestDataHelper {

    public static <T> List<T> loadTestData(String filePath, Class<T> type) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] jsonData = Files.readAllBytes(Paths.get(filePath));
        return objectMapper.readValue(jsonData, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }
}
