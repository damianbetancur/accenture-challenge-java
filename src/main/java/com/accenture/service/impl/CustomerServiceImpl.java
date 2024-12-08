package com.accenture.service.impl;

import com.accenture.dto.CustomerDTO;
import com.accenture.dto.mapper.CustomerMapper;
import com.accenture.repository.CustomerRepository;
import com.accenture.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Optional<CustomerDTO> findByCustomerId(String customerId) {
        return customerRepository.findByCustomerId(customerId)
                .map(customerMapper::toDTO);
    }
}
