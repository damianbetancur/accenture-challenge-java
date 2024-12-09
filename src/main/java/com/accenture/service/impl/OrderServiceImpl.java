package com.accenture.service.impl;

import com.accenture.dto.mapper.CustomerMapper;
import com.accenture.dto.mapper.ProductMapper;
import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;
import com.accenture.exception.OrderAlreadyProcessedException;
import com.accenture.exception.OrderNotFoundException;
import com.accenture.model.Customer;
import com.accenture.model.Order;
import com.accenture.model.OrderLine;
import com.accenture.model.Product;
import com.accenture.model.enums.OrderStatus;
import com.accenture.repository.OrderRepository;
import com.accenture.service.CustomerService;
import com.accenture.service.OrderService;
import com.accenture.service.ProductService;
import com.accenture.strategy.impl.OrderStatusStrategyManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final OrderStatusStrategyManager statusManager;
    private final ThreadPoolExecutor threadPoolExecutor;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductService productService,
                            ProductMapper productMapper,
                            CustomerService customerService,
                            CustomerMapper customerMapper,
                            OrderStatusStrategyManager statusManager,
                            ThreadPoolExecutor threadPoolExecutor) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.productMapper = productMapper;
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.statusManager = statusManager;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public CompletableFuture<OrderResponse> processOrderAsync(OrderRequest orderRequest) {
        if (threadPoolExecutor.getQueue() != null && threadPoolExecutor.getQueue().remainingCapacity() == 0) {
            throw new IllegalStateException("ThreadPoolExecutor queue is full. Cannot accept more tasks.");
        }

        log.info("Received async order processing request for Order ID: {}", orderRequest.getIdOrder());
        return CompletableFuture.supplyAsync(() -> processOrder(orderRequest), threadPoolExecutor)
                .handle((response, ex) -> {
                    if (ex != null) {
                        log.error("Failed to process order for Order ID: {}. Error: {}", orderRequest.getIdOrder(), ex.getMessage());
                        throw new RuntimeException(ex);
                    }
                    return response;
                });
    }

    public OrderResponse processOrder(OrderRequest orderRequest) {
        log.info("Processing order ID: {}", orderRequest.getIdOrder());

        if (orderRepository.isProcessed(orderRequest.getIdOrder())) {
            log.warn("Order ID {} already processed", orderRequest.getIdOrder());
            throw new OrderAlreadyProcessedException("Order with ID " + orderRequest.getIdOrder() + " has already been processed.");
        }

        Order order = validateOrder(orderRequest);
        simulateProcessingDelay(order);

        orderRepository.save(order);
        log.info("Order ID {} saved successfully", order.getOrderId());

        return mapToOrderResponse(order);
    }

    private void simulateProcessingDelay(Order order) {
        try {
            log.info("Processing delay for Order ID: {}", order.getOrderId());
            order.setStatus(OrderStatus.PROCESSING);
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
            order.setStatus(statusManager.determineStatus(order));
            log.info("Processing completed for Order ID: {}", order.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Order processing interrupted for Order ID: {}", order.getOrderId(), e);
            throw new OrderNotFoundException("Order processing interrupted", e);
        }
    }

    private Order validateOrder(OrderRequest orderRequest) {
        List<OrderLine> orderLines = orderRequest.getOrderLineRequestList().stream()
                .map(request -> {
                    Product product = productService.findBySku(request.getSku())
                            .map(productMapper::toEntity)
                            .orElse(null);
                    return new OrderLine(product, request.getQuantity());
                })
                .collect(Collectors.toList());

        Customer customer = customerService.findByCustomerId(orderRequest.getCustomerId())
                .map(customerMapper::toEntity)
                .orElse(null);

        return new Order(
                orderRequest.getIdOrder(),
                customer,
                orderRequest.getOrderAmount(),
                OrderStatus.PENDING,
                orderLines
        );
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomer() != null ? order.getCustomer().getCustomerId() : "Unknown Customer",
                order.getOrderAmount(),
                order.getStatus().getDescription()
        );
    }
}
