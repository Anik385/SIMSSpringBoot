package com.example.Inventory.system.mapper;

import com.example.Inventory.system.dto.requests.CustomerRequest;
import com.example.Inventory.system.dto.response.CustomerResponse;
import com.example.Inventory.system.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse toResponse(Customer customer);
    List<CustomerResponse> toResponseList(List<Customer> customers);
    void updateEntity(@MappingTarget Customer customer, CustomerRequest request);
}
