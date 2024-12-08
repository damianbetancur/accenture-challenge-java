package com.accenture.repository.impl;

import com.accenture.model.Order;
import com.accenture.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    // Mapa sincronizado para gestionar los pedidos procesados
    // Evita bloqueos globales, mejorando el rendimiento en sistemas de alta concurrencia
    private final Map<String, Order> processedOrders = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        processedOrders.put(order.getOrderId(), order);
    }

    @Override
    public boolean isProcessed(String orderId) {
        return processedOrders.containsKey(orderId);
    }

}
