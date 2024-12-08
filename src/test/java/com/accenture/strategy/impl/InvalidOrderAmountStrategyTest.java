package com.accenture.strategy.impl;

import com.accenture.model.Customer;
import com.accenture.model.Order;
import com.accenture.model.OrderLine;
import com.accenture.model.Product;
import com.accenture.model.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvalidOrderAmountStrategyTest {

    private final InvalidOrderAmountStrategy strategy = new InvalidOrderAmountStrategy();

    @Test
    @DisplayName("Crear una orden con monto cero")
    void testIsApplicable_OrderAmountZero() {
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Product product = new Product("P001", "Laptop", 1200.50);
        OrderLine orderLine = new OrderLine(product, 1);
        Order order = new Order("123", customer, 0.0, OrderStatus.PENDING, List.of(orderLine));

        boolean result = strategy.isApplicable(order);

        assertTrue(result, "Strategy should be applicable when order amount is zero");
    }

    @Test
    @DisplayName("Crear una orden con monto negativo")
    void testIsApplicable_OrderAmountNegative() {
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Product product = new Product("P001", "Laptop", 1200.50);
        OrderLine orderLine = new OrderLine(product, 1);
        Order order = new Order("123", customer, -10.0, OrderStatus.PENDING, List.of(orderLine));

        boolean result = strategy.isApplicable(order);

        assertTrue(result, "Strategy should be applicable when order amount is negative");
    }

    @Test
    @DisplayName("Crear una orden con monto válido")
    void testIsApplicable_ValidOrderAmount() {
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Product product = new Product("P001", "Laptop", 1200.50);
        OrderLine orderLine = new OrderLine(product, 1);
        Order order = new Order("123", customer, 1200.50, OrderStatus.PENDING, List.of(orderLine));

        boolean result = strategy.isApplicable(order);

        assertFalse(result, "Strategy should not be applicable when order amount is valid");
    }

    @Test
    void testGetStatus() {
        OrderStatus status = strategy.getStatus();

        assertSame(status, OrderStatus.INVALID_AMOUNT, "Order amount is invalid");
    }
}
