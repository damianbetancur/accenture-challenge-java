package com.accenture.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Order has been received but not yet processed"),
    PROCESSING("Order is currently being processed"),
    COMPLETED("Order has been successfully processed"),
    CUSTOMER_NOT_FOUND("Customer not found during order processing"),
    PRODUCT_NOT_FOUND("One or more products not found during order processing"),
    INVALID_AMOUNT("Order amount is invalid"),
    INVALID_LINES("Order lines contain invalid data"),
    FAILED("Order processing failed");


    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}