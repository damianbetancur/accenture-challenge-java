package com.accenture.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class OrderLineRequest {

    @NotBlank(message = "SKU is required")
    private String sku;

    @Positive(message = "Quantity must be greater than zero")
    private int quantity;

}
