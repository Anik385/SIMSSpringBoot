package com.example.Inventory.system.dto.response;

public record LocationResponse(
        Long id,
        String aisle,
        String shelf,
        String bin,
        String description
) {}