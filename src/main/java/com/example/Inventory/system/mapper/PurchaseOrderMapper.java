package com.example.Inventory.system.mapper;

import com.example.Inventory.system.dto.response.PurchaseOrderItemResponse;
import com.example.Inventory.system.dto.response.PurchaseOrderResponse;
import com.example.Inventory.system.entity.PurchaseOrder;
import com.example.Inventory.system.entity.PurchaseOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    PurchaseOrderResponse toResponse(PurchaseOrder order);

    List<PurchaseOrderResponse> toResponseList(List<PurchaseOrder> orders);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productSku", source = "product.sku")
    PurchaseOrderItemResponse toItemResponse(PurchaseOrderItem item);
}
