package com.accenture.model;

import com.accenture.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderId;
    private Customer customer;
    private double orderAmount;
    private OrderStatus status;
    private List<OrderLine> orderLines;

}
