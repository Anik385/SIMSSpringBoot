package com.example.Inventory.system.repository;

import com.example.Inventory.system.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
