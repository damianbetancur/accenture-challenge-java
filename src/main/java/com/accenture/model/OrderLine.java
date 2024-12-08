package com.accenture.model;

import lombok.Data;

@Data
public class OrderLine {
    private Product product;
    private int quantity;
    private double subtotal;

    public OrderLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.subtotal = calculateSubtotal();
    }

    private double calculateSubtotal() {
        if (product != null && quantity > 0) {
            return product.getPrice() * quantity;
        }
        return 0.0;
    }
}
