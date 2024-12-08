package com.accenture.dto.mapper;

import com.accenture.dto.ProductDTO;
import com.accenture.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDTO(product.getSku(), product.getName(), product.getPrice());
    }

    public Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        return new Product(productDTO.getSku(), productDTO.getName(), productDTO.getPrice());
    }
}

