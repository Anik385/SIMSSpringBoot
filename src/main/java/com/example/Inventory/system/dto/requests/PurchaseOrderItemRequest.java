package com.example.Inventory.system.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PurchaseOrderItemRequest(
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity,
        BigDecimal unitPrice
) {}
