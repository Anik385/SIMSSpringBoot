package com.example.Inventory.system.dto.response;

import com.example.Inventory.system.entity.PurchaseOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderResponse(
        Long id,
        String orderNumber,
        Long supplierId,
        String supplierName,
        PurchaseOrderStatus status,
        BigDecimal totalAmount,
        String notes,
        LocalDateTime expectedDelivery,
        LocalDateTime createdAt,
        List<PurchaseOrderItemResponse> items
) {}
