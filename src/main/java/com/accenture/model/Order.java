package com.accenture.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @NotBlank(message = "Customer ID cannot be empty")
    private String customerId;

    @NotBlank(message = "Price list ID cannot be empty")
    private String priceListId;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItem> orderItems;

    @NotNull(message = "Total amount cannot be null")
    private double totalAmount;

    private LocalDateTime createdAt = LocalDateTime.now();
}
