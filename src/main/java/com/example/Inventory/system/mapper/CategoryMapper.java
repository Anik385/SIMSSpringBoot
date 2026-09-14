package com.example.Inventory.system.mapper;

import com.example.Inventory.system.dto.response.CategoryResponse;
import com.example.Inventory.system.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    List<CategoryResponse> toResponseList(List<Category> categories);

    void updateEntity(@MappingTarget Category category, com.example.Inventory.system.dto.requests.CategoryRequest request);
}