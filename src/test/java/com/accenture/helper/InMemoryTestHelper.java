package com.accenture.helper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class InMemoryTestHelper {

    public static <T> Map<String, T> createInMemoryRepo(List<T> data, KeyExtractor<T> keyExtractor) {
        Map<String, T> inMemoryRepo = new ConcurrentHashMap<>();
        data.forEach(item -> inMemoryRepo.put(keyExtractor.extractKey(item), item));
        return inMemoryRepo;
    }

    @FunctionalInterface
    public interface KeyExtractor<T> {
        String extractKey(T item);
    }
}
