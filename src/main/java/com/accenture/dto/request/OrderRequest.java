package com.accenture.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.Positive;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Order ID is required")
    @JsonProperty("id_order")
    private String idOrder;

    @NotBlank(message = "Customer ID is required")
    @JsonProperty("customer_id")
    private String customerId;

    @Positive(message = "Order amount must be greater than zero")
    @JsonProperty("order_amount")
    private double orderAmount;

    @NotEmpty(message = "Order must contain at least one item")
    @JsonProperty("order_lines")
    private List<@Valid OrderLineRequest> orderLineRequestList;
}
