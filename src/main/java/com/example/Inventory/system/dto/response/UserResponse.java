package com.example.Inventory.system.dto.response;

import com.example.Inventory.system.entity.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        Role role,
        LocalDateTime createdAt
) {}