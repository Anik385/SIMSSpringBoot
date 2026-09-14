package com.example.Inventory.system.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record LocationRequest(
        @NotBlank String aisle,
        String shelf,
        String bin,
        String description
) {}