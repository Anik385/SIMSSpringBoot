package com.example.Inventory.system.dto.response;

import java.math.BigDecimal;

public record TopSellingProductResponse(
        Long productId,
        String productName,
        String sku,
        Long totalQuantitySold,
        BigDecimal totalRevenue
) {}
