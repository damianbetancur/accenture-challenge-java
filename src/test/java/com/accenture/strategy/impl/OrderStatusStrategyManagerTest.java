package com.accenture.strategy.impl;

import com.accenture.model.Customer;
import com.accenture.model.Order;
import com.accenture.model.OrderLine;
import com.accenture.model.Product;
import com.accenture.model.enums.OrderStatus;
import com.accenture.strategy.OrderStatusStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStatusStrategyManagerTest {

    private OrderStatusStrategyManager strategyManager;
    private OrderStatusStrategy completedStrategy;
    private OrderStatusStrategy failedStrategy;

    @BeforeEach
    void setup() {
        completedStrategy = Mockito.mock(OrderStatusStrategy.class);
        failedStrategy = Mockito.mock(OrderStatusStrategy.class);
        strategyManager = new OrderStatusStrategyManager(List.of(completedStrategy, failedStrategy));
    }

    @Test
    @DisplayName("orden válida")
    void testDetermineStatus_WithApplicableStrategy() {
        Order order = createValidOrder();

        Mockito.when(completedStrategy.isApplicable(order)).thenReturn(true);
        Mockito.when(completedStrategy.getStatus()).thenReturn(OrderStatus.COMPLETED);
        Mockito.when(failedStrategy.isApplicable(order)).thenReturn(false);

        OrderStatus result = strategyManager.determineStatus(order);

        assertEquals(OrderStatus.COMPLETED, result);
    }

    @Test
    @DisplayName("orden inválida")
    void testDetermineStatus_WithoutApplicableStrategy() {
        Order order = createInvalidOrder();

        Mockito.when(completedStrategy.isApplicable(order)).thenReturn(false);
        Mockito.when(failedStrategy.isApplicable(order)).thenReturn(false);

        OrderStatus result = strategyManager.determineStatus(order);

        assertEquals(OrderStatus.FAILED, result);
    }

    private Order createValidOrder() {
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Product product = new Product("P001", "Laptop", 1200.50);
        OrderLine orderLine = new OrderLine(product, 1);

        return new Order("123", customer, 1200.50, OrderStatus.PENDING, List.of(orderLine));
    }

    private Order createInvalidOrder() {
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        return new Order("123", customer, 0.0, OrderStatus.PENDING, List.of());
    }
}
