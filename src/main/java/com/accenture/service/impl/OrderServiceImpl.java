package com.accenture.service.impl;

import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;
import com.accenture.exception.OrderAlreadyProcessedException;
import com.accenture.exception.OrderNotFoundException;
import com.accenture.dto.mapper.CustomerMapper;
import com.accenture.dto.mapper.ProductMapper;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final OrderStatusStrategyManager statusManager;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductService productService,
                            ProductMapper productMapper,
                            CustomerService customerService,
                            CustomerMapper customerMapper,
                            OrderStatusStrategyManager statusManager) {

        this.orderRepository = orderRepository;
        this.productService = productService;
        this.productMapper = productMapper;
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.statusManager = statusManager;
    }

    @Override
    public OrderResponse processOrder(OrderRequest orderRequest) {
        // Validar si la orden ya fue procesada
        if (orderRepository.isProcessed(orderRequest.getIdOrder())) {
            throw new OrderAlreadyProcessedException("Order with ID " + orderRequest.getIdOrder() + " has already been processed.");
        }

        // Transformar OrderRequest a Order con estado PENDING
        Order order = validateOrder(orderRequest);

        // Simular retraso en el procesamiento
        simulateProcessingDelay(order);

        // Almacenar en el repositorio
        orderRepository.save(order);

        // Transformar Order a OrderResponse
        return mapToOrderResponse(order);
    }

    private void simulateProcessingDelay(Order order) {
        try {
            order.setStatus(OrderStatus.PROCESSING);
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
            // Determinar el estado de la orden
            order.setStatus(statusManager.determineStatus(order));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OrderNotFoundException("Order processing interrupted", e);
        }
    }


    private Order validateOrder(OrderRequest orderRequest) {
        List<OrderLine> orderLines = orderRequest.getOrderLineRequestList().stream()
                .map(request -> {
                    Product product = productService.findBySku(request.getSku())
                            .map(productMapper::toEntity)
                            .orElse(null); // Si no se encuentra, simplemente se asigna null
                    return new OrderLine(product, request.getQuantity());
                })
                .collect(Collectors.toList());

        Customer customer = customerService.findByCustomerId(orderRequest.getCustomerId())
                .map(customerMapper::toEntity)
                .orElse(null); // Si no se encuentra, simplemente se asigna null

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
