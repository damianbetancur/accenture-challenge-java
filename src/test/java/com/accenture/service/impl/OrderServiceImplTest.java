package com.accenture.service.impl;

import com.accenture.dto.CustomerDTO;
import com.accenture.dto.ProductDTO;
import com.accenture.dto.mapper.CustomerMapper;
import com.accenture.dto.mapper.ProductMapper;
import com.accenture.dto.request.OrderLineRequest;
import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;
import com.accenture.model.Order;
import com.accenture.model.enums.OrderStatus;
import com.accenture.repository.OrderRepository;
import com.accenture.service.CustomerService;
import com.accenture.service.ProductService;
import com.accenture.strategy.impl.OrderStatusStrategyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

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

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ThreadPoolExecutor threadPoolExecutor;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);

        threadPoolExecutor = new ThreadPoolExecutor(
                50, // Tamaño mínimo del grupo de hilos
                100, // Tamaño máximo del grupo de hilos
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000) // Capacidad de la cola
        );


        orderService = new OrderServiceImpl(
                orderRepository,
                productService,
                productMapper,
                customerService,
                customerMapper,
                statusManager,
                threadPoolExecutor
        );
    }

    @Test
    void processOrder_Success() throws InterruptedException, ExecutionException, TimeoutException {
        OrderRequest orderRequest = createOrderRequest();
        OrderResponse expectedResponse = createOrderResponse();

        // Configurar los mocks
        when(orderRepository.isProcessed(orderRequest.getIdOrder())).thenReturn(false);
        when(customerService.findByCustomerId(anyString()))
                .thenReturn(Optional.of(new CustomerDTO("C001", "John Doe", "john@example.com", "1234567890", "123 Main St")));
        when(productService.findBySku("P001"))
                .thenReturn(Optional.of(new ProductDTO("P001", "Laptop", 1200.0)));
        when(productService.findBySku("P002"))
                .thenReturn(Optional.of(new ProductDTO("P002", "Mouse", 20.0)));
        when(statusManager.determineStatus(any(Order.class)))
                .thenReturn(OrderStatus.COMPLETED);

        // Enviar la tarea al ExecutorService
        CompletableFuture<OrderResponse> responseFuture = orderService.processOrderAsync(orderRequest);

        // Esperar el resultado
        OrderResponse response = responseFuture.get(10, TimeUnit.SECONDS);

        // Verificar el resultado
        assertNotNull(response);
        assertEquals(expectedResponse.getOrderId(), response.getOrderId());
        assertEquals(expectedResponse.getTotalAmount(), response.getTotalAmount());
        assertEquals(expectedResponse.getStatus(), response.getStatus());

        // Verificar que se guardó en el repositorio
        verify(orderRepository, times(1)).save(any(Order.class));

        // Apagar el ThreadPoolExecutor y verificar
        threadPoolExecutor.shutdown();
        assertTrue(threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS), "El ThreadPoolExecutor no terminó en el tiempo esperado.");
    }






    private OrderRequest createOrderRequest() {
        return new OrderRequest(
                "O001",
                "C001",
                1220.0,
                Arrays.asList(
                        new OrderLineRequest("P001", 1),
                        new OrderLineRequest("P002", 1)
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

    @Test
    void testConcurrentProcessingAndValidation() {
        int numberOfRequests = 1000;

        when(statusManager.determineStatus(any(Order.class))).thenReturn(OrderStatus.COMPLETED);

        // Lista para almacenar los futuros
        List<CompletableFuture<OrderResponse>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfRequests; i++) {
            OrderRequest request = new OrderRequest("O" + i, "C001", 100.0, List.of());
            futures.add(orderService.processOrderAsync(request));
        }

        // Esperar que todas las tareas terminen
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Verificar que todas las órdenes fueron guardadas
        for (int i = 0; i < numberOfRequests; i++) {
            String orderId = "O" + i;
            verify(orderRepository, times(1)).save(argThat(order -> order.getOrderId().equals(orderId)));
        }
    }
}
