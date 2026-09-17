package com.example.Inventory.system.repository;

import com.example.Inventory.system.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByIsActiveTrue();
    boolean existsByName(String name);
}
