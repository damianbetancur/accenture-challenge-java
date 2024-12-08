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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidLinesStatusStrategyTest {

    private final InvalidLinesStatusStrategy strategy = new InvalidLinesStatusStrategy();

    @Test
    void testIsApplicable_EmptyOrderLines() {
        // Arrange: Crear una orden sin líneas de pedido
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 100.0, OrderStatus.PENDING, Collections.emptyList());

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertTrue(result, "Strategy should be applicable when order lines are empty");
    }

    @Test
    void testIsApplicable_InvalidOrderLineProductNull() {
        // Arrange: Crear una orden con una línea de pedido inválida (producto nulo)
        OrderLine invalidLine = new OrderLine(null, 1);
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 100.0, OrderStatus.PENDING, List.of(invalidLine));

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertTrue(result, "Strategy should be applicable when a product in an order line is null");
    }

    @Test
    void testIsApplicable_InvalidOrderLineQuantityZero() {
        // Arrange: Crear una orden con una línea de pedido inválida (cantidad = 0)
        Product product = new Product("P001", "Laptop", 1200.50);
        OrderLine invalidLine = new OrderLine(product, 0);
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 100.0, OrderStatus.PENDING, List.of(invalidLine));

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertTrue(result, "Strategy should be applicable when an order line has a quantity <= 0");
    }

    @Test
    void testIsApplicable_ValidOrderLines() {
        // Arrange: Crear una orden con líneas de pedido válidas
        Product product1 = new Product("P001", "Laptop", 1200.50);
        Product product2 = new Product("P002", "Smartphone", 800.75);
        OrderLine line1 = new OrderLine(product1, 1);
        OrderLine line2 = new OrderLine(product2, 2);
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 2802.0, OrderStatus.PENDING, Arrays.asList(line1, line2));

        // Act: Verificar si la estrategia es aplicable
        boolean result = strategy.isApplicable(order);

        // Assert
        assertFalse(result, "Strategy should not be applicable for valid order lines");
    }

    @Test
    void testGetStatus() {
        // Act: Obtener el estado de la estrategia
        OrderStatus status = strategy.getStatus();

        // Assert
        assertTrue(status == OrderStatus.INVALID_LINES, "Order lines contain invalid data");
    }
}
