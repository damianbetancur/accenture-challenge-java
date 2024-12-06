package com.accenture.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "priceLists")
public class PriceList {

    @Id
    private String id;

    @NotBlank(message = "Price list name cannot be empty")
    private String name;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    private boolean active;

    @NotNull(message = "Items cannot be null")
    private List<PriceListItem> items;
}
