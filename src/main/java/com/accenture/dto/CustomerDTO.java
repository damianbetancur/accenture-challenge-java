package com.accenture.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    @JsonProperty("customer_id")
    private String customerId;

    private String name;
    private String email;
    private String phone;
    private String address;
}
