package com.example.Inventory.system.controller;

import com.example.Inventory.system.dto.requests.LocationRequest;
import com.example.Inventory.system.dto.response.LocationResponse;
import com.example.Inventory.system.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    @Operation(summary = "Get all locations")
    public List<LocationResponse> getAll() {
        return locationService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get location by ID")
    public LocationResponse getById(@PathVariable Long id) {
        return locationService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a location")
    public LocationResponse create(@Valid @RequestBody LocationRequest request) {
        return locationService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update a location")
    public LocationResponse update(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        return locationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a location")
    public void delete(@PathVariable Long id) {
        locationService.delete(id);
    }
}
