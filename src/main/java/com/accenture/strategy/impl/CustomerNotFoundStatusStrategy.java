package com.accenture.strategy.impl;

import com.accenture.model.Order;
import com.accenture.model.enums.OrderStatus;
import com.accenture.strategy.OrderStatusStrategy;
import org.springframework.stereotype.Component;

@Component
public class CustomerNotFoundStatusStrategy implements OrderStatusStrategy {

    @Override
    public boolean isApplicable(Order order) {
        return order.getCustomer() == null;
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.CUSTOMER_NOT_FOUND;
    }
}
