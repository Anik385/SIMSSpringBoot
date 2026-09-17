package com.example.Inventory.system.controller;

import com.example.Inventory.system.dto.response.*;
import com.example.Inventory.system.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get aggregated dashboard stats")
    public DashboardStatsResponse getDashboardStats() {
        return reportService.getDashboardStats();
    }

    @GetMapping("/top-selling")
    @Operation(summary = "Get top selling products")
    public List<TopSellingProductResponse> getTopSelling(@RequestParam(defaultValue = "5") int limit) {
        return reportService.getTopSellingProducts(limit);
    }

    @GetMapping("/sales-trend")
    @Operation(summary = "Get daily sales trend for last N days")
    public List<SalesTrendResponse> getSalesTrend(@RequestParam(defaultValue = "7") int days) {
        return reportService.getSalesTrend(days);
    }

    @GetMapping("/category-stock")
    @Operation(summary = "Get stock grouped by category")
    public List<CategoryStockResponse> getCategoryStock() {
        return reportService.getCategoryStock();
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products")
    public List<LowStockProductResponse> getLowStock() {
        return reportService.getLowStockProducts();
    }
}
