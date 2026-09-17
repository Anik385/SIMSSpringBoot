package com.example.Inventory.system.mapper;

import com.example.Inventory.system.dto.requests.SupplierRequest;
import com.example.Inventory.system.dto.response.SupplierResponse;
import com.example.Inventory.system.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierResponse toResponse(Supplier supplier);
    List<SupplierResponse> toResponseList(List<Supplier> suppliers);
    void updateEntity(@MappingTarget Supplier supplier, SupplierRequest request);
}