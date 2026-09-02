package com.example.Inventory.system.dto.requests;

import jakarta.validation.constraints.NotNull;

public record StockAdjustRequest(
        @NotNull Long productId,
        @NotNull Integer delta,  // positive or negative
        @NotNull String reason   // must match MovementReason enum
) {
}
