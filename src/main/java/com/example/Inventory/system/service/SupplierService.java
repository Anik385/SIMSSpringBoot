package com.example.Inventory.system.service;

import com.example.Inventory.system.dto.requests.SupplierRequest;
import com.example.Inventory.system.dto.response.SupplierResponse;
import com.example.Inventory.system.entity.Supplier;
import com.example.Inventory.system.exception.ResourceNotFoundException;
import com.example.Inventory.system.mapper.SupplierMapper;
import com.example.Inventory.system.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public List<SupplierResponse> getAll() {
        return supplierMapper.toResponseList(supplierRepository.findAll());
    }

    public List<SupplierResponse> getActive() {
        return supplierMapper.toResponseList(supplierRepository.findByIsActiveTrue());
    }

    public SupplierResponse getById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
        return supplierMapper.toResponse(supplier);
    }

    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .contactPerson(request.contactPerson())
                .isActive(request.isActive() == null ? true : request.isActive())
                .build();
        return supplierMapper.toResponse(supplierRepository.save(supplier));
    }

    @Transactional
    public SupplierResponse update(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
        supplierMapper.updateEntity(supplier, request);
        return supplierMapper.toResponse(supplierRepository.save(supplier));
    }

    @Transactional
    public void delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found: " + id);
        }
        supplierRepository.deleteById(id);
    }
}