package com.example.Inventory.system.mapper;

import com.example.Inventory.system.dto.response.SaleItemResponse;
import com.example.Inventory.system.dto.response.SaleResponse;
import com.example.Inventory.system.entity.Sale;
import com.example.Inventory.system.entity.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "customerId", source = "sale.customer.id")
    @Mapping(target = "customerName", source = "sale.customer.name")
    @Mapping(target = "soldById", source = "sale.soldBy.id")
    @Mapping(target = "soldByName", source = "sale.soldBy.fullName")
    SaleResponse toResponse(Sale sale);

    List<SaleResponse> toResponseList(List<Sale> sales);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productSku", source = "product.sku")
    SaleItemResponse toItemResponse(SaleItem item);
}
