package com.example.Inventory.system.service;

import com.example.Inventory.system.dto.requests.LocationRequest;
import com.example.Inventory.system.dto.response.LocationResponse;
import com.example.Inventory.system.entity.Location;
import com.example.Inventory.system.exception.ResourceNotFoundException;
import com.example.Inventory.system.mapper.LocationMapper;
import com.example.Inventory.system.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public List<LocationResponse> getAll() {
        return locationMapper.toResponseList(locationRepository.findAll());
    }

    public LocationResponse getById(Long id) {
        Location loc = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + id));
        return locationMapper.toResponse(loc);
    }

    @Transactional
    public LocationResponse create(LocationRequest request) {
        Location location = Location.builder()
                .aisle(request.aisle())
                .shelf(request.shelf())
                .bin(request.bin())
                .description(request.description())
                .build();
        return locationMapper.toResponse(locationRepository.save(location));
    }

    @Transactional
    public LocationResponse update(Long id, LocationRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + id));
        locationMapper.updateEntity(location, request);
        return locationMapper.toResponse(locationRepository.save(location));
    }

    @Transactional
    public void delete(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Location not found: " + id);
        }
        locationRepository.deleteById(id);
    }
}
