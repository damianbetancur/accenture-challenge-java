package com.accenture.repository;


import com.accenture.model.Order;

import java.util.Map;

public interface OrderRepository {
    void save(Order order);
    boolean isProcessed(String orderId);
}
