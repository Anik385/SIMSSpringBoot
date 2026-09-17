package com.example.Inventory.system.service;

import com.example.Inventory.system.dto.requests.CustomerRequest;
import com.example.Inventory.system.dto.response.CustomerResponse;
import com.example.Inventory.system.entity.Customer;
import com.example.Inventory.system.exception.ResourceNotFoundException;
import com.example.Inventory.system.mapper.CustomerMapper;
import com.example.Inventory.system.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerResponse> getAll() {
        return customerMapper.toResponseList(customerRepository.findAll());
    }

    public CustomerResponse getById(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        return customerMapper.toResponse(c);
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (request.email() != null && customerRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use: " + request.email());
        }
        Customer customer = Customer.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .loyaltyPoints(0)
                .isActive(request.isActive() == null ? true : request.isActive())
                .build();
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        customerMapper.updateEntity(customer, request);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }
}
