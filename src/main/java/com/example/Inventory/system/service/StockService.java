package com.example.Inventory.system.service;

import com.example.Inventory.system.dto.requests.StockAdjustRequest;
import com.example.Inventory.system.entity.MovementReason;
import com.example.Inventory.system.entity.Product;
import com.example.Inventory.system.entity.StockMovement;
import com.example.Inventory.system.entity.User;
import com.example.Inventory.system.exception.InsufficientStockException;
import com.example.Inventory.system.exception.ResourceNotFoundException;
import com.example.Inventory.system.repository.ProductRepository;
import com.example.Inventory.system.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;
    private final StockMovementRepository movementRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public StockMovement adjustStock(StockAdjustRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if we have enough stock for negative adjustments
        if (product.getQuantity() + request.delta() < 0) {
            throw new InsufficientStockException(
                    "Not enough stock. Current: " + product.getQuantity() + ", Requested delta: " + request.delta()
            );
        }

        // Update product quantity
        product.setQuantity(product.getQuantity() + request.delta());
        productRepository.save(product);

        // Get current user from Security Context
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Create movement record
        StockMovement movement = StockMovement.builder()
                .product(product)
                .user(currentUser)
                .quantityChange(request.delta())
                .reason(MovementReason.valueOf(request.reason().toUpperCase()))
                .notes("Adjusted via API")
                .build();

        StockMovement saved = movementRepository.save(movement);

        // Broadcast real-time update via WebSocket
        messagingTemplate.convertAndSend(
                "/topic/stock-updates",
                Map.of(
                        "productId", product.getId(),
                        "sku", product.getSku(),
                        "newQuantity", product.getQuantity(),
                        "delta", request.delta()
                )
        );

        return saved;
    }
}