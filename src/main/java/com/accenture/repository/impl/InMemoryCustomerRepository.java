package com.accenture.repository.impl;

import com.accenture.model.Customer;
import com.accenture.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> clientStorage = new ConcurrentHashMap<>();

    @Override
    public Optional<Customer> findByCustomerId(String customerId) {
        return Optional.ofNullable(clientStorage.get(customerId));
    }

    @Override
    public void save(Customer customer) {
        clientStorage.put(customer.getCustomerId(), customer);
    }

}
