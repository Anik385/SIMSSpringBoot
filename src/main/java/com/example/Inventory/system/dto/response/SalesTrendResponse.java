package com.example.Inventory.system.dto.response;

import java.math.BigDecimal;

public record SalesTrendResponse(
        String period,        // "2026-09-01"
        long saleCount,
        BigDecimal revenue
) {}
