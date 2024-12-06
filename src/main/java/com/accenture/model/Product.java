package com.accenture.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @Positive(message = "Base price must be greater than 0")
    private double basePrice;

    @NotBlank(message = "Category cannot be empty")
    private String category;
}