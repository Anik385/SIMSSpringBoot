package com.example.Inventory.system.controller;

import com.example.Inventory.system.dto.requests.SaleRequest;
import com.example.Inventory.system.dto.response.SaleResponse;
import com.example.Inventory.system.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    @Operation(summary = "Get all sales")
    public List<SaleResponse> getAll() {
        return saleService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by ID")
    public SaleResponse getById(@PathVariable Long id) {
        return saleService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    @Operation(summary = "Create a sale (deducts stock)")
    public SaleResponse create(@Valid @RequestBody SaleRequest request) {
        return saleService.create(request);
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Refund a sale (restores stock)")
    public SaleResponse refund(@PathVariable Long id) {
        return saleService.refund(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a sale")
    public void delete(@PathVariable Long id) {
        saleService.delete(id);
    }
}
