package com.accenture.strategy.impl;

import com.accenture.model.Order;
import com.accenture.model.OrderLine;
import com.accenture.model.enums.OrderStatus;
import com.accenture.strategy.OrderStatusStrategy;
import org.springframework.stereotype.Component;

@Component
public class InvalidLinesStatusStrategy implements OrderStatusStrategy {

    @Override
    public boolean isApplicable(Order order) {
        return order.getOrderLines() == null ||
                order.getOrderLines().isEmpty() ||
                order.getOrderLines().stream().anyMatch(line ->
                        line.getProduct() == null ||
                                line.getProduct().getSku() == null ||
                                line.getQuantity() <= 0 ||
                                line.getProduct().getPrice() <= 0
                );
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.INVALID_LINES;
    }
}

