package com.example.Inventory.system.service;

import com.example.Inventory.system.dto.response.*;
import com.example.Inventory.system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final SaleRepository saleRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public DashboardStatsResponse getDashboardStats() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        return new DashboardStatsResponse(
                productRepository.count(),
                customerRepository.count(),
                supplierRepository.count(),
                saleRepository.countCompletedSales(),
                saleRepository.getTotalRevenue(),
                saleRepository.getRevenueSince(startOfToday),
                saleRepository.getRevenueSince(startOfMonth),
                productRepository.countLowStockProducts(),
                purchaseOrderRepository.countPendingPurchaseOrders()
        );
    }

    public List<TopSellingProductResponse> getTopSellingProducts(int limit) {
        List<Object[]> results = saleRepository.findTopSellingProducts(PageRequest.of(0, limit));
        List<TopSellingProductResponse> list = new ArrayList<>();
        for (Object[] row : results) {
            list.add(new TopSellingProductResponse(
                    ((Number) row[0]).longValue(),
                    (String) row[1],
                    (String) row[2],
                    ((Number) row[3]).longValue(),
                    (BigDecimal) row[4]
            ));
        }
        return list;
    }

    public List<SalesTrendResponse> getSalesTrend(int days) {
        LocalDateTime start = LocalDate.now().minusDays(days).atStartOfDay();
        List<Object[]> results = saleRepository.findSalesTrendSince(start);
        List<SalesTrendResponse> list = new ArrayList<>();
        for (Object[] row : results) {
            list.add(new SalesTrendResponse(
                    row[0].toString(),
                    ((Number) row[1]).longValue(),
                    (BigDecimal) row[2]
            ));
        }
        return list;
    }

    public List<CategoryStockResponse> getCategoryStock() {
        List<Object[]> results = productRepository.findCategoryStockSummary();
        List<CategoryStockResponse> list = new ArrayList<>();
        for (Object[] row : results) {
            String category = row[0] != null ? row[0].toString() : "Uncategorized";
            list.add(new CategoryStockResponse(
                    category,
                    ((Number) row[1]).longValue(),
                    ((Number) row[2]).longValue()
            ));
        }
        return list;
    }

    public List<LowStockProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProductsOrdered().stream()
                .map(p -> new LowStockProductResponse(
                        p.getId(), p.getName(), p.getSku(),
                        p.getQuantity(), p.getReorderThreshold()))
                .toList();
    }
}
