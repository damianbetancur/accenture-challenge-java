package com.accenture.service.impl;


import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;
import com.accenture.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    public OrderResponse processOrder(OrderRequest orderRequest) {

        String orderId = orderRequest.getIdOrder();
        String customerId = orderRequest.getCustomerId();
        double totalAmount = orderRequest.getOrderAmount();

        return new OrderResponse(orderId, customerId, totalAmount, "Order successfully processed");
    }
}