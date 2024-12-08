package com.accenture.strategy.impl;

import com.accenture.model.Customer;
import com.accenture.model.Order;
import com.accenture.model.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CustomerNotFoundStatusStrategyTest {

    private final CustomerNotFoundStatusStrategy strategy = new CustomerNotFoundStatusStrategy();

    @Test
    void testIsApplicable_CustomerIsNull() {
        Order order = new Order("123", null, 100.0, OrderStatus.PENDING, Collections.emptyList());

        boolean result = strategy.isApplicable(order);

        assertTrue(result, "Strategy should be applicable when customer is null");
    }

    @Test
    void testIsApplicable_CustomerIsPresent() {
        Customer customer = new Customer("C001", "John Doe", "john.doe@example.com", "+1234567890", "123 Main Street");
        Order order = new Order("123", customer, 100.0, OrderStatus.PENDING, Collections.emptyList());

        boolean result = strategy.isApplicable(order);

        assertFalse(result, "Strategy should not be applicable when customer is present");
    }

    @Test
    void testGetStatus() {
        OrderStatus status = strategy.getStatus();

        assertSame(status, OrderStatus.CUSTOMER_NOT_FOUND, "Customer not found during order processing");
    }
}
