package com.example.Inventory.system.repository;

import com.example.Inventory.system.entity.Sale;
import com.example.Inventory.system.entity.SaleStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findBySaleNumber(String saleNumber);
    List<Sale> findByCustomerId(Long customerId);
    List<Sale> findByStatus(SaleStatus status);

    @Query("SELECT s FROM Sale s WHERE s.createdAt BETWEEN :start AND :end ORDER BY s.createdAt DESC")
    List<Sale> findSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.status = 'COMPLETED' AND s.createdAt BETWEEN :start AND :end")
    java.math.BigDecimal getTotalSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.status = 'COMPLETED'")
    long countCompletedSales();

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.status = 'COMPLETED'")
    java.math.BigDecimal getTotalRevenue();

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
            "WHERE s.status = 'COMPLETED' AND s.createdAt >= :start")
    java.math.BigDecimal getRevenueSince(@Param("start") LocalDateTime start);

    // Top selling products
    @Query("SELECT si.product.id, si.product.name, si.product.sku, " +
            "SUM(si.quantity), SUM(si.subtotal) " +
            "FROM SaleItem si WHERE si.sale.status = 'COMPLETED' " +
            "GROUP BY si.product.id, si.product.name, si.product.sku " +
            "ORDER BY SUM(si.quantity) DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);

    // Sales trend (grouped by day)
    @Query(value = "SELECT DATE(s.created_at) as day, COUNT(*) as cnt, " +
            "COALESCE(SUM(s.total_amount), 0) as revenue " +
            "FROM sales s WHERE s.status = 'COMPLETED' " +
            "AND s.created_at >= :start " +
            "GROUP BY DATE(s.created_at) ORDER BY day ASC",
            nativeQuery = true)
    List<Object[]> findSalesTrendSince(@Param("start") LocalDateTime start);
}
