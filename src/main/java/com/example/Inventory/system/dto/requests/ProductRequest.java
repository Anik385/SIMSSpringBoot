package com.example.Inventory.system.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        String description,
        String category,
        @NotNull @Min(0) BigDecimal price,
        @NotNull @Min(0) Integer quantity,
        Integer reorderThreshold,
        Long locationId

) {
}
