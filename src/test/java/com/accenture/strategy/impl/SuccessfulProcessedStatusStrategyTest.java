package com.accenture.strategy.impl;

import com.accenture.model.Customer;
import com.accenture.model.Order;
import com.accenture.model.OrderLine;
import com.accenture.model.Product;
import com.accenture.model.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SuccessfulProcessedStatusStrategyTest {

    private final SuccessfulProcessedStatusStrategy strategy = new SuccessfulProcessedStatusStrategy();

    @Test
    void testIsApplicable_ValidOrder() {
        // Arrange: Crear una orden válida
        Product product1 = new Product("P001", "Laptop", 1200.50);
        Product product2 = new Product("P002", "Smartphone", 800.75);
        OrderLine line1 = new OrderLine(product1, 1);
        OrderLine line2 = new OrderLine(product2, 2);
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 2802.0, OrderStatus.PENDING, Arrays.asList(line1, line2));

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertTrue(result, "Strategy should be applicable for a valid order");
    }

    @Test
    void testIsApplicable_NullCustomer() {
        // Arrange: Crear una orden con cliente nulo
        Product product = new Product("P001", "Laptop", 1200.50);
        OrderLine line = new OrderLine(product, 1);
        Order order = new Order("123", null, 1200.50, OrderStatus.PENDING, List.of(line));

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertFalse(result, "Strategy should not be applicable when customer is null");
    }

    @Test
    void testIsApplicable_InvalidOrderAmount() {
        // Arrange: Crear una orden con monto de pedido no válido
        Product product = new Product("P001", "Laptop", 1200.50);
        OrderLine line = new OrderLine(product, 1);
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 0.0, OrderStatus.PENDING, List.of(line));

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertFalse(result, "Strategy should not be applicable when order amount is zero or less");
    }

    @Test
    void testIsApplicable_EmptyOrderLines() {
        // Arrange: Crear una orden sin líneas de pedido
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 100.0, OrderStatus.PENDING, Collections.emptyList());

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertFalse(result, "Strategy should not be applicable when order lines are empty");
    }

    @Test
    void testIsApplicable_InvalidOrderLine() {
        // Arrange: Crear una orden con una línea de pedido inválida
        OrderLine invalidLine = new OrderLine(null, 0);
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 100.0, OrderStatus.PENDING, List.of(invalidLine));

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertFalse(result, "Strategy should not be applicable when an order line is invalid");
    }

    @Test
    void testGetStatus() {
        // Act: Obtener el estado de la estrategia
        OrderStatus status = strategy.getStatus();

        // Assert
        assertSame(status, OrderStatus.COMPLETED, "Status should be COMPLETED for successfully processed orders");
    }
}
