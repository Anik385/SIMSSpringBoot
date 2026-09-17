package com.example.Inventory.system.controller;

import com.example.Inventory.system.dto.requests.SupplierRequest;
import com.example.Inventory.system.dto.response.SupplierResponse;
import com.example.Inventory.system.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    @Operation(summary = "Get all suppliers")
    public List<SupplierResponse> getAll() {
        return supplierService.getAll();
    }

    @GetMapping("/active")
    @Operation(summary = "Get active suppliers only")
    public List<SupplierResponse> getActive() {
        return supplierService.getActive();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID")
    public SupplierResponse getById(@PathVariable Long id) {
        return supplierService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a supplier")
    public SupplierResponse create(@Valid @RequestBody SupplierRequest request) {
        return supplierService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update a supplier")
    public SupplierResponse update(@PathVariable Long id, @Valid @RequestBody SupplierRequest request) {
        return supplierService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a supplier")
    public void delete(@PathVariable Long id) {
        supplierService.delete(id);
    }
}
