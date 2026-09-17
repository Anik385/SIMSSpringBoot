package com.example.Inventory.system.service;

import com.example.Inventory.system.dto.requests.PurchaseOrderItemRequest;
import com.example.Inventory.system.dto.requests.PurchaseOrderRequest;
import com.example.Inventory.system.dto.response.PurchaseOrderResponse;
import com.example.Inventory.system.entity.*;
import com.example.Inventory.system.entity.MovementReason;
import com.example.Inventory.system.entity.PurchaseOrderStatus;
import com.example.Inventory.system.exception.ResourceNotFoundException;
import com.example.Inventory.system.mapper.PurchaseOrderMapper;
import com.example.Inventory.system.repository.ProductRepository;
import com.example.Inventory.system.repository.PurchaseOrderRepository;
import com.example.Inventory.system.repository.StockMovementRepository;
import com.example.Inventory.system.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    private String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "PO-" + datePart;
    }

    public List<PurchaseOrderResponse> getAll() {
        return purchaseOrderMapper.toResponseList(purchaseOrderRepository.findAll());
    }

    public PurchaseOrderResponse getById(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found: " + id));
        return purchaseOrderMapper.toResponse(po);
    }

    @Transactional
    public PurchaseOrderResponse create(PurchaseOrderRequest request) {
        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PurchaseOrder order = PurchaseOrder.builder()
                .orderNumber(generateOrderNumber())
                .supplier(supplier)
                .createdBy(currentUser)
                .status(PurchaseOrderStatus.DRAFT)
                .notes(request.notes())
                .expectedDelivery(request.expectedDelivery())
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseOrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.productId()));

            BigDecimal unitPrice = itemReq.unitPrice() != null
                    ? itemReq.unitPrice()
                    : product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.quantity()));

            PurchaseOrderItem item = PurchaseOrderItem.builder()
                    .purchaseOrder(order)
                    .product(product)
                    .quantity(itemReq.quantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            order.getItems().add(item);
            total = total.add(subtotal);
        }

        order.setTotalAmount(total);
        return purchaseOrderMapper.toResponse(purchaseOrderRepository.save(order));
    }

    @Transactional
    public PurchaseOrderResponse updateStatus(Long id, PurchaseOrderStatus status) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found: " + id));

        // When receiving, increase product stock and log movements
        if (status == PurchaseOrderStatus.RECEIVED && order.getStatus() != PurchaseOrderStatus.RECEIVED) {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            for (PurchaseOrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);

                StockMovement movement = StockMovement.builder()
                        .product(product)
                        .user(currentUser)
                        .quantityChange(item.getQuantity())
                        .reason(MovementReason.RECEIVED)
                        .notes("Received from PO " + order.getOrderNumber())
                        .build();
                stockMovementRepository.save(movement);
            }
        }

        order.setStatus(status);
        return purchaseOrderMapper.toResponse(purchaseOrderRepository.save(order));
    }

    @Transactional
    public void delete(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found: " + id));
        if (order.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new IllegalStateException("Cannot delete a received purchase order");
        }
        purchaseOrderRepository.deleteById(id);
    }
}
