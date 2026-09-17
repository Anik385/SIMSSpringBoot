package com.example.Inventory.system.dto.response;

import java.math.BigDecimal;

public record DashboardStatsResponse(
        long totalProducts,
        long totalCustomers,
        long totalSuppliers,
        long totalSales,
        BigDecimal totalRevenue,
        BigDecimal todayRevenue,
        BigDecimal monthRevenue,
        long lowStockCount,
        long pendingPurchaseOrders
) {}
