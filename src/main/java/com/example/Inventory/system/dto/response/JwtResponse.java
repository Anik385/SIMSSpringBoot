package com.example.Inventory.system.dto.response;

public record JwtResponse(
        String token,
        String email,
        String role
) {
}
