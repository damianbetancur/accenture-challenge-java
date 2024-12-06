package com.accenture.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PriceListItem {

    @NotBlank(message = "Product code cannot be empty")
    private String productCode;

    @Positive(message = "Price must be greater than 0")
    private double price;
}
