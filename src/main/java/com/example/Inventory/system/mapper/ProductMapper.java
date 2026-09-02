package com.example.Inventory.system.mapper;
import com.example.Inventory.system.dto.requests.ProductRequest;
import com.example.Inventory.system.dto.response.ProductResponse;
import com.example.Inventory.system.entity.Location;
import com.example.Inventory.system.entity.Product;
import org.mapstruct.*;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "location", expression = "java(location)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequest request, @Context Location location);

    @Mapping(target = "locationId", source = "product.location.id")
    @Mapping(target = "locationDescription", source = "product.location.description")
    ProductResponse toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "location", expression = "java(location)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Product product, ProductRequest request, @Context Location location);
}
