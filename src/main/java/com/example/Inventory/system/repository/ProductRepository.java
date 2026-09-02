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

//    @Query("SELECT p FROM Product p WHERE p.quantity <= p.reorderThreshold")
//    List<Product> findLowStockProducts();   // Keep this name or rename as you like

}