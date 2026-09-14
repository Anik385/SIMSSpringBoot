package com.example.Inventory.system.mapper;

import com.example.Inventory.system.dto.requests.LocationRequest;
import com.example.Inventory.system.dto.response.LocationResponse;
import com.example.Inventory.system.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationResponse toResponse(Location location);
    List<LocationResponse> toResponseList(List<Location> locations);

    void updateEntity(@MappingTarget Location location, LocationRequest request);
}
