package com.accenture.controller;

import com.accenture.dto.request.OrderItemRequest;
import com.accenture.dto.request.OrderRequest;
import com.accenture.dto.response.OrderResponse;
import com.accenture.helper.OrdersTestHelper;
import com.accenture.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Test
    public void testMalformedJsonRequest() throws Exception {
        String invalidJson = "{ \"id_order\": \"123\", \"customer_id\": \"456\", \"order_amount\": 100.50, \"orderItems\": [ }";

        mockMvc.perform(post("/orders/processOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    public void testProcessOrder_Success() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setIdOrder("123");
        orderRequest.setCustomerId("456");
        orderRequest.setOrderAmount(100.50);
        orderRequest.setOrderItems(List.of(new OrderItemRequest("1", 2, 25.0), new OrderItemRequest("2", 1, 50.5)));

        OrderResponse expectedResponse = new OrderResponse("123", "456", 100.50, "Order successfully processed");

        Mockito.when(orderService.processOrder(Mockito.any(OrderRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/orders/processOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isCreated()) // HTTP 201
                .andExpect(jsonPath("$.orderId").value("123"))
                .andExpect(jsonPath("$.customerId").value("456"))
                .andExpect(jsonPath("$.totalAmount").value(100.50))
                .andExpect(jsonPath("$.status").value("Order successfully processed"));
    }

    @Test
    void testOrdersTestHelperLoadsData() {
        Map<String, Object> normalOrder = OrdersTestHelper.getNormalOrderJsonMap();
        assertNotNull(normalOrder);
        assertEquals("123", normalOrder.get("id_order"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidOrders")
    void testProcessOrder_FailWithInvalidData(Map<String, Object> jsonInput) throws Exception {
        String requestJson = new ObjectMapper().writeValueAsString(jsonInput);

        mockMvc.perform(post("/orders/processOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> provideInvalidOrders() {
        return Stream.of(
                Arguments.of(
                        OrdersTestHelper.getInvalidIdOrderEmptyJsonMap()
                ),
                Arguments.of(
                        OrdersTestHelper.getInvalidOrderEmptyItemsJsonMap()
                )
        );
    }
}