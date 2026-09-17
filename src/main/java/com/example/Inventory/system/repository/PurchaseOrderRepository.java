package com.example.Inventory.system.repository;

import com.example.Inventory.system.entity.PurchaseOrder;
import com.example.Inventory.system.entity.PurchaseOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    List<PurchaseOrder> findByStatus(PurchaseOrderStatus status);
    List<PurchaseOrder> findBySupplierId(Long supplierId);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.status IN ('DRAFT', 'PENDING_APPROVAL', 'APPROVED')")
    long countPendingPurchaseOrders();
}
