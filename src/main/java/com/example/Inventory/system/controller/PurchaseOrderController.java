package com.example.Inventory.system.controller;

import com.example.Inventory.system.dto.requests.PurchaseOrderRequest;
import com.example.Inventory.system.dto.response.PurchaseOrderResponse;
import com.example.Inventory.system.entity.PurchaseOrderStatus;
import com.example.Inventory.system.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @GetMapping
    @Operation(summary = "Get all purchase orders")
    public List<PurchaseOrderResponse> getAll() {
        return purchaseOrderService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order by ID")
    public PurchaseOrderResponse getById(@PathVariable Long id) {
        return purchaseOrderService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a purchase order")
    public PurchaseOrderResponse create(@Valid @RequestBody PurchaseOrderRequest request) {
        return purchaseOrderService.create(request);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update purchase order status (APPROVED, RECEIVED, CANCELLED)")
    public PurchaseOrderResponse updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        PurchaseOrderStatus status = PurchaseOrderStatus.valueOf(body.get("status").toUpperCase());
        return purchaseOrderService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a draft purchase order")
    public void delete(@PathVariable Long id) {
        purchaseOrderService.delete(id);
    }
}
