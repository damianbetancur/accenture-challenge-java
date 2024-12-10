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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class DataLoader {

    @Value("${data.customers.file:data/customers.json}")
    private String CUSTOMER_DATA_FILE;

    @Value("${data.products.file:data/products.json}")
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

    private void loadProducts() {
        try {
            log.info("Loading products from {}", PRODUCT_DATA_FILE);
            // Usar ClassLoader para cargar desde el classpath
            try (var inputStream = getClass().getClassLoader().getResourceAsStream(PRODUCT_DATA_FILE)) {
                if (inputStream == null) {
                    throw new IOException("File not found in classpath: data/products.json");
                }
                List<Product> products = objectMapper.readValue(inputStream, new TypeReference<>() {
                });
                products.forEach(productRepository::save);
                log.info("Successfully loaded {} products.", products.size());
            }
        } catch (IOException e) {
            log.error("Failed to load products from {}: {}", PRODUCT_DATA_FILE, e.getMessage());
        }
    }


    private void loadCustomers() {
        try {
            log.info("Loading customers from {}", CUSTOMER_DATA_FILE);
            // Usar ClassLoader para cargar desde el classpath
            try (var inputStream = getClass().getClassLoader().getResourceAsStream(CUSTOMER_DATA_FILE)) {
                if (inputStream == null) {
                    throw new IOException("File not found in classpath: data/customers.json");
                }
                List<Customer> customers = objectMapper.readValue(inputStream, new TypeReference<>() {
                });
                customers.forEach(customerRepository::save);
                log.info("Successfully loaded {} customers.", customers.size());
            }
        } catch (IOException e) {
            log.error("Failed to load customers from {}: {}", CUSTOMER_DATA_FILE, e.getMessage());
        }
    }
}
