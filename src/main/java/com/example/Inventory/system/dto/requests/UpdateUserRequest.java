package com.example.Inventory.system.dto.requests;

import com.example.Inventory.system.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotBlank String fullName,
        @NotNull Role role
) {}
