package com.accenture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    //Stock Keeping Unit (Unidad de Mantenimiento de Inventario)
    private String sku;
    private String name;
    private double price;
}
