package com.accenture.service;

import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse processOrder(OrderRequest orderRequest);
}
