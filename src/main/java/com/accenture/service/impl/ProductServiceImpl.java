package com.accenture.service.impl;

import com.accenture.dto.ProductDTO;
import com.accenture.dto.mapper.ProductMapper;
import com.accenture.repository.ProductRepository;
import com.accenture.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Optional<ProductDTO> findBySku(String sku) {
        return productRepository.findBySku(sku)
                .map(productMapper::toDTO);
    }
}
