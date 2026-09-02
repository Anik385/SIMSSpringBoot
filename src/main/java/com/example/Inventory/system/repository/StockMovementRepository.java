package com.example.Inventory.system.repository;

import com.example.Inventory.system.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement,Long> {
    List<StockMovement> findByProductId(Long productId);
}
