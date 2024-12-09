package com.accenture.service;

import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;

import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OrderResponse> processOrderAsync(OrderRequest orderRequest);
}
