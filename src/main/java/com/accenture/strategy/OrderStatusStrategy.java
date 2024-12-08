package com.accenture.strategy;

import com.accenture.model.Order;
import com.accenture.model.enums.OrderStatus;

public interface OrderStatusStrategy {
    boolean isApplicable(Order order);
    OrderStatus getStatus();
}
