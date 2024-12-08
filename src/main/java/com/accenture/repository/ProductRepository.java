package com.accenture.repository;

import com.accenture.model.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findBySku(String sku);
    void save(Product product);
}
