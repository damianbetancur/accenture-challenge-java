package com.accenture.dto.mapper;

import com.accenture.dto.CustomerDTO;
import com.accenture.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress()
        );
    }

    public Customer toEntity(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }
        return new Customer(
                customerDTO.getCustomerId(),
                customerDTO.getName(),
                customerDTO.getEmail(),
                customerDTO.getPhone(),
                customerDTO.getAddress()
        );
    }
}
