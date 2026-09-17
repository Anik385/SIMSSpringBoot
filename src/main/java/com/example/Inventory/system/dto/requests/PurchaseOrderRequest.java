package com.example.Inventory.system.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderRequest(
        @NotNull Long supplierId,
        LocalDateTime expectedDelivery,
        String notes,
        @NotEmpty List<PurchaseOrderItemRequest> items
) {}
