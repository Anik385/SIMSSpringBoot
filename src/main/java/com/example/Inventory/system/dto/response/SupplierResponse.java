package com.example.Inventory.system.dto.response;

import java.time.LocalDateTime;

public record SupplierResponse(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        String contactPerson,
        Boolean isActive,
        LocalDateTime createdAt
) {}
