package com.example.Inventory.system.dto.response;

import com.example.Inventory.system.entity.PaymentMethod;
import com.example.Inventory.system.entity.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponse(
        Long id,
        String saleNumber,
        Long customerId,
        String customerName,
        Long soldById,
        String soldByName,
        SaleStatus status,
        PaymentMethod paymentMethod,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal totalAmount,
        String notes,
        LocalDateTime createdAt,
        List<SaleItemResponse> items
) {}