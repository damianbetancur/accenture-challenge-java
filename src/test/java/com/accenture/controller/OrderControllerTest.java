package com.accenture.controller;

import com.accenture.helper.InMemoryTestHelper;
import com.accenture.helper.OrdersTestHelper;
import com.accenture.helper.TestDataHelper;
import com.accenture.model.Customer;
import com.accenture.model.Product;
import com.accenture.repository.CustomerRepository;
import com.accenture.repository.ProductRepository;
import com.accenture.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setupInMemoryRepos() throws IOException {
        // Crear repositorio en memoria para productos
        List<Product> products = TestDataHelper.loadTestData("src/test/resources/data/products.json", Product.class);
        Map<String, Product> productRepo = InMemoryTestHelper.createInMemoryRepo(products, Product::getSku);
        productRepo.forEach((key, value) -> Mockito.when(productRepository.findBySku(key)).thenReturn(Optional.of(value)));

        // Crear repositorio en memoria para clientes
        List<Customer> customers = TestDataHelper.loadTestData("src/test/resources/data/customers.json", Customer.class);
        Map<String, Customer> customerRepo = InMemoryTestHelper.createInMemoryRepo(customers, Customer::getCustomerId);
        customerRepo.forEach((key, value) -> Mockito.when(customerRepository.findByCustomerId(key)).thenReturn(Optional.of(value)));
    }

    @Test
    public void testMalformedJsonRequest() throws Exception {
        String invalidJson = "{ \"id_order\": \"123\", \"customer_id\": \"456\", \"order_amount\": 100.50, \"orderItems\": [ }";

        mockMvc.perform(post("/orders/processOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @ParameterizedTest
    @MethodSource("provideAllsOrders")
    void testProcessOrder_Success(Map<String, Object> jsonInput) throws Exception {

        String requestJson = new ObjectMapper().writeValueAsString(jsonInput);

        mockMvc.perform(post("/orders/processOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
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
                        OrdersTestHelper.getInvalidOrderItemsEmptyJsonMap()
                )
        );
    }

    static Stream<Arguments> provideAllsOrders() {
        return Stream.of(
                Arguments.of(
                        OrdersTestHelper.getNormalOrderJsonMap()
                )
        );
    }
}