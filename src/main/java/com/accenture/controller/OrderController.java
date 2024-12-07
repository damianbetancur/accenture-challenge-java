package com.accenture.controller;

import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;
import com.accenture.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/processOrder")
    public ResponseEntity<OrderResponse> processOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.processOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
