package com.accenture.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class OrderItemRequest {

    @NotBlank(message = "Product ID is required")
    @JsonProperty("product_id")
    private String productId;

    @Positive(message = "Quantity must be greater than zero")
    private int quantity;

    @Positive(message = "Price must be greater than zero")
    private double price;
}
