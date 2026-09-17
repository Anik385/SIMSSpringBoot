package com.example.Inventory.system.dto.response;

import java.math.BigDecimal;

public record SaleItemResponse(
        Long id,
        Long productId,
        String productName,
        String productSku,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
