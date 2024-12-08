package com.accenture.repository.impl;

import com.accenture.model.Product;
import com.accenture.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> productStorage = new ConcurrentHashMap<>();

    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(productStorage.get(sku));
    }

    @Override
    public void save(Product product) {
        productStorage.put(product.getSku(), product);
    }
}
