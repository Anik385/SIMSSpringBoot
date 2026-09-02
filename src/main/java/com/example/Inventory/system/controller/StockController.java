package com.example.Inventory.system.controller;


import com.example.Inventory.system.dto.requests.StockAdjustRequest;
import com.example.Inventory.system.entity.StockMovement;
import com.example.Inventory.system.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StockController {

    private final StockService stockService;

    @PostMapping("/adjust")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    @Operation(summary = "Adjust stock (scanning/inventory)")
    public StockMovement adjustStock(@Valid @RequestBody StockAdjustRequest request) {
        return stockService.adjustStock(request);
    }
}