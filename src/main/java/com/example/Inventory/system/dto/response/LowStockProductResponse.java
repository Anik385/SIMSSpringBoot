package com.example.Inventory.system.dto.response;

public record LowStockProductResponse(
        Long productId,
        String name,
        String sku,
        Integer quantity,
        Integer reorderThreshold
) {}
