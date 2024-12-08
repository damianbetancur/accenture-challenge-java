package com.accenture.strategy.impl;

import com.accenture.model.Order;
import com.accenture.model.enums.OrderStatus;
import com.accenture.strategy.OrderStatusStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrderStatusStrategyManager {

    private final List<OrderStatusStrategy> strategies;

    public OrderStatusStrategyManager(List<OrderStatusStrategy> strategies) {
        this.strategies = strategies;
        logStrategies(); // Agrega un log al inicializar
    }

    private void logStrategies() {
        if (strategies == null || strategies.isEmpty()) {
            log.info("No strategies were registered!");
        } else {
            log.info("Registered strategies:");
            strategies.forEach(strategy -> log.info(strategy.getClass().getSimpleName()));
        }
    }

    public OrderStatus determineStatus(Order order) {
        return strategies.stream()
                .filter(strategy -> strategy.isApplicable(order))
                .findFirst()
                .map(OrderStatusStrategy::getStatus)
                .orElse(OrderStatus.FAILED);
    }
}

