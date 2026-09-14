package com.example.Inventory.system.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        String category,
        BigDecimal price,
        Integer quantity,
        Integer reorderThreshold,
        Long locationId,
        String locationDescription,
        Long categoryId
) {
}
