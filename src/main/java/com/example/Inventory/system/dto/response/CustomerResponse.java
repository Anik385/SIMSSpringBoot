package com.example.Inventory.system.dto.response;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        Integer loyaltyPoints,
        Boolean isActive,
        LocalDateTime createdAt
) {}
