package com.accenture.config;

import com.accenture.model.Customer;
import com.accenture.model.Product;
import com.accenture.repository.CustomerRepository;
import com.accenture.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
@Profile("local")
public class DataLoader {

    @Value("${data.customers.file:src/main/resources/data/customers.json}")
    private String CUSTOMER_DATA_FILE;

    @Value("${data.products.file:src/main/resources/data/products.json}")
    private String PRODUCT_DATA_FILE;

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public DataLoader(CustomerRepository customerRepository, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadData() {
        loadCustomers();
        loadProducts();
    }

    private void loadCustomers() {
        try {
            log.info("Loading customers from {}", CUSTOMER_DATA_FILE);
            String json = new String(Files.readAllBytes(Paths.get(CUSTOMER_DATA_FILE)));
            List<Customer> customers = objectMapper.readValue(json, new TypeReference<>() {});
            customers.forEach(customerRepository::save);
            log.info("Successfully loaded {} customers.", customers.size());
        } catch (IOException e) {
            log.error("Failed to load customers from {}: {}", CUSTOMER_DATA_FILE, e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            log.info("Loading products from {}", PRODUCT_DATA_FILE);
            String json = new String(Files.readAllBytes(Paths.get(PRODUCT_DATA_FILE)));
            List<Product> products = objectMapper.readValue(json, new TypeReference<>() {});
            products.forEach(productRepository::save);
            log.info("Successfully loaded {} products.", products.size());
        } catch (IOException e) {
            log.error("Failed to load products from {}: {}", PRODUCT_DATA_FILE, e.getMessage());
        }
    }
}
