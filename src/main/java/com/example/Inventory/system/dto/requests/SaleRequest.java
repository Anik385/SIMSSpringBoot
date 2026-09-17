package com.example.Inventory.system.dto.requests;

import com.example.Inventory.system.entity.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record SaleRequest(
        Long customerId,          // can be null for walk-in
        @NotNull PaymentMethod paymentMethod,
        BigDecimal discount,
        BigDecimal tax,
        String notes,
        @NotEmpty List<SaleItemRequest> items
) {}
