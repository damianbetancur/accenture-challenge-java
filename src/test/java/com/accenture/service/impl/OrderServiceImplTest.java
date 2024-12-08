package com.accenture.service.impl;

import com.accenture.dto.CustomerDTO;
import com.accenture.dto.ProductDTO;
import com.accenture.dto.mapper.CustomerMapper;
import com.accenture.dto.mapper.ProductMapper;
import com.accenture.dto.request.OrderLineRequest;
import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;
import com.accenture.exception.OrderAlreadyProcessedException;
import com.accenture.model.Order;
import com.accenture.model.enums.OrderStatus;
import com.accenture.repository.OrderRepository;
import com.accenture.service.CustomerService;
import com.accenture.service.ProductService;
import com.accenture.strategy.impl.OrderStatusStrategyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private CustomerService customerService;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private OrderStatusStrategyManager statusManager;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() throws IOException {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);

        orderService = new OrderServiceImpl(
                orderRepository,
                productService,
                productMapper,
                customerService,
                customerMapper,
                statusManager
        );
    }

    @Test
    void processOrder_Success() {
        OrderRequest orderRequest = createOrderRequest();
        OrderResponse expectedResponse = createOrderResponse();

        when(orderRepository.isProcessed(orderRequest.getIdOrder())).thenReturn(false);
        when(customerService.findByCustomerId(anyString())).thenReturn(Optional.of(new CustomerDTO("C001", "John Doe", "john@example.com", "1234567890", "123 Main St")));
        when(productService.findBySku(anyString())).thenReturn(Optional.of(new ProductDTO("P001", "Laptop", 1200.0)));
        when(productService.findBySku(anyString())).thenReturn(Optional.of(new ProductDTO("P002", "Mouse", 20.0)));
        when(statusManager.determineStatus(any(Order.class))).thenReturn(OrderStatus.COMPLETED);

        OrderResponse response = orderService.processOrder(orderRequest);

        assertNotNull(response);
        assertEquals(expectedResponse.getOrderId(), response.getOrderId());
        assertEquals(expectedResponse.getTotalAmount(), response.getTotalAmount());
        assertEquals(expectedResponse.getStatus(), response.getStatus());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void processOrder_AlreadyProcessed() {
        OrderRequest orderRequest = createOrderRequest();

        when(orderRepository.isProcessed(orderRequest.getIdOrder())).thenReturn(true);

        assertThrows(OrderAlreadyProcessedException.class, () -> orderService.processOrder(orderRequest));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void processOrder_InvalidCustomer() {
        OrderRequest orderRequest = createOrderRequest();

        when(orderRepository.isProcessed(orderRequest.getIdOrder())).thenReturn(false);
        when(customerService.findByCustomerId("C001")).thenReturn(Optional.empty());
        when(productService.findBySku("P001")).thenReturn(Optional.of(new ProductDTO("P001", "Laptop", 1200.0)));
        when(productService.findBySku("P002")).thenReturn(Optional.of(new ProductDTO("P002", "Mouse", 20.0)));

        when(statusManager.determineStatus(any(Order.class))).thenReturn(OrderStatus.FAILED);

        OrderResponse response = orderService.processOrder(orderRequest);

        assertNotNull(response);
        assertEquals("Unknown Customer", response.getCustomerId());
        assertEquals(OrderStatus.FAILED.getDescription(), response.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void processOrder_InvalidProducts() {
        OrderRequest orderRequest = createOrderRequest();

        when(orderRepository.isProcessed(orderRequest.getIdOrder())).thenReturn(false);
        when(customerService.findByCustomerId("C001")).thenReturn(Optional.of(new CustomerDTO("C001", "John Doe", "john@example.com", "1234567890", "123 Main St")));
        when(productService.findBySku("P001")).thenReturn(Optional.empty());
        when(productService.findBySku("P002")).thenReturn(Optional.empty());

        when(statusManager.determineStatus(any(Order.class))).thenReturn(OrderStatus.FAILED);

        OrderResponse response = orderService.processOrder(orderRequest);

        assertNotNull(response);
        assertEquals(OrderStatus.FAILED.getDescription(), response.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    private OrderRequest createOrderRequest() {
        return new OrderRequest(
                "O001",
                "C001",
                1220.0,
                Arrays.asList(
                        new OrderLineRequest("P001", 1, 1200.0),
                        new OrderLineRequest("P002", 1, 20.0)
                )
        );
    }

    private OrderResponse createOrderResponse() {
        return new OrderResponse(
                "O001",
                "C001",
                1220.0,
                OrderStatus.COMPLETED.getDescription()
        );
    }
}
