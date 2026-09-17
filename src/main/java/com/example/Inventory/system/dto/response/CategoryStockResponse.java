package com.example.Inventory.system.dto.response;

public record CategoryStockResponse(
        String categoryName,
        long productCount,
        long totalStock
) {}
