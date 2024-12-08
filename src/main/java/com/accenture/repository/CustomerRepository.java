package com.accenture.repository;

import com.accenture.model.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findByCustomerId(String customerId);
    void save(Customer customer);
}
