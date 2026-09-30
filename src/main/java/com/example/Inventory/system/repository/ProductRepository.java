package com.example.Inventory.system.repository;

import com.example.Inventory.system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);

    @Query(value = "SELECT * FROM products WHERE quantity <= reorder_threshold", nativeQuery = true)
    List<Product> findLowStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity <= p.reorderThreshold")
    long countLowStockProducts();

    @Query("SELECT p FROM Product p WHERE p.quantity <= p.reorderThreshold ORDER BY p.quantity ASC")
    List<Product> findLowStockProductsOrdered();

    // New (uses the actual entity field)
//    @Query("SELECT p.categoryId, COUNT(p), COALESCE(SUM(p.quantity), 0) " +
//            "FROM Product p GROUP BY p.categoryId")
//    List<Object[]> findCategoryStockSummary();

    @Query("SELECT c.name, COUNT(p), COALESCE(SUM(p.quantity), 0) " +
            "FROM Product p LEFT JOIN Category c ON p.categoryId = c.id " +
            "GROUP BY c.name")
    List<Object[]> findCategoryStockSummary();

//    @Query("SELECT p.category, COUNT(p), COALESCE(SUM(p.quantity), 0) " +
//            "FROM Product p GROUP BY p.category")
//    List<Object[]> findCategoryStockSummary();

//    @Query("SELECT p FROM Product p WHERE p.quantity <= p.reorderThreshold")
//    List<Product> findLowStockProducts();   // Keep this name or rename as you like

}