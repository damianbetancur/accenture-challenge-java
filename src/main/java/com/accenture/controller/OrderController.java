package com.accenture.controller;

import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;
import com.accenture.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/processOrder")
    public CompletableFuture<ResponseEntity<OrderResponse>> processOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.processOrderAsync(orderRequest)
                .thenApply(ResponseEntity::ok);
    }
}
