package com.accenture.strategy.impl;

import com.accenture.model.Order;
import com.accenture.model.OrderLine;
import com.accenture.model.enums.OrderStatus;
import com.accenture.strategy.OrderStatusStrategy;
import org.springframework.stereotype.Component;

@Component
public class SuccessfulProcessedStatusStrategy implements OrderStatusStrategy {

    @Override
    public boolean isApplicable(Order order) {
        // La estrategia es aplicable si la orden tiene:
        // 1. Cliente no nulo
        // 2. Montos y cantidades válidas
        // 3. Líneas de pedido con productos válidos
        return order.getCustomer() != null &&
                order.getOrderAmount() > 0 &&
                order.getOrderLines() != null &&
                !order.getOrderLines().isEmpty() &&
                order.getOrderLines().stream().allMatch(this::isValidOrderLine);
    }

    private boolean isValidOrderLine(OrderLine orderLine) {
        return orderLine.getProduct() != null &&
                orderLine.getProduct().getSku() != null &&
                orderLine.getQuantity() > 0 &&
                orderLine.getProduct().getPrice() > 0;
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.COMPLETED;
    }
}
