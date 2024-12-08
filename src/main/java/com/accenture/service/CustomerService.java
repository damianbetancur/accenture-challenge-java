package com.accenture.service;

import com.accenture.dto.CustomerDTO;

import java.util.Optional;

public interface CustomerService {
    Optional<CustomerDTO> findByCustomerId(String customerId);
}
