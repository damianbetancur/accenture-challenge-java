package com.accenture.strategy.impl;

import com.accenture.model.Order;
import com.accenture.model.enums.OrderStatus;
import com.accenture.strategy.OrderStatusStrategy;
import org.springframework.stereotype.Component;

@Component
public class InvalidOrderAmountStrategy implements OrderStatusStrategy {

    @Override
    public boolean isApplicable(Order order) {
        if (order.getOrderLines() == null || order.getOrderLines().isEmpty()) {
            return false; // Si no hay líneas, esta validación no se aplica.
        }

        double calculatedAmount = order.getOrderLines().stream()
                .mapToDouble(line -> line.getQuantity() * (line.getProduct() != null ? line.getProduct().getPrice() : 0))
                .sum();

        return Double.compare(order.getOrderAmount(), calculatedAmount) != 0;
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.INVALID_AMOUNT; // La orden falla si el monto no coincide con las líneas.
    }
}
