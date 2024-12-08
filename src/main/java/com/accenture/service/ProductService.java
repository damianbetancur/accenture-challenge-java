package com.accenture.service;

import com.accenture.dto.ProductDTO;

import java.util.Optional;

public interface ProductService {
    Optional<ProductDTO> findBySku(String sku);
}
