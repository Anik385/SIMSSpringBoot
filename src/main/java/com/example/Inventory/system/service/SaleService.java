package com.example.Inventory.system.service;

import com.example.Inventory.system.dto.requests.SaleItemRequest;
import com.example.Inventory.system.dto.requests.SaleRequest;
import com.example.Inventory.system.dto.response.SaleResponse;
import com.example.Inventory.system.entity.*;
import com.example.Inventory.system.entity.MovementReason;
import com.example.Inventory.system.entity.SaleStatus;
import com.example.Inventory.system.exception.InsufficientStockException;
import com.example.Inventory.system.exception.ResourceNotFoundException;
import com.example.Inventory.system.mapper.SaleMapper;
import com.example.Inventory.system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final StockMovementRepository stockMovementRepository;
    private final SaleMapper saleMapper;
    private final SimpMessagingTemplate messagingTemplate;

    private String generateSaleNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "SALE-" + datePart;
    }

    public List<SaleResponse> getAll() {
        return saleMapper.toResponseList(saleRepository.findAll());
    }

    public SaleResponse getById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found: " + id));
        return saleMapper.toResponse(sale);
    }

    @Transactional
    public SaleResponse create(SaleRequest request) {
        // 1. Validate customer (if provided)
        Customer customer = null;
        if (request.customerId() != null) {
            customer = customerRepository.findById(request.customerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        }

        // 2. Get current user
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 3. Create sale object
        Sale sale = Sale.builder()
                .saleNumber(generateSaleNumber())
                .customer(customer)
                .soldBy(currentUser)
                .status(SaleStatus.COMPLETED)
                .paymentMethod(request.paymentMethod())
                .discount(request.discount() != null ? request.discount() : BigDecimal.ZERO)
                .tax(request.tax() != null ? request.tax() : BigDecimal.ZERO)
                .notes(request.notes())
                .items(new ArrayList<>())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;

        // 4. Process each item – validate stock, deduct, log movement
        for (SaleItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.productId()));

            if (product.getQuantity() < itemReq.quantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for " + product.getName() +
                                ". Available: " + product.getQuantity() +
                                ", Requested: " + itemReq.quantity()
                );
            }

            BigDecimal unitPrice = itemReq.unitPrice() != null
                    ? itemReq.unitPrice()
                    : product.getPrice();
            BigDecimal lineSubtotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.quantity()));

            SaleItem item = SaleItem.builder()
                    .sale(sale)
                    .product(product)
                    .quantity(itemReq.quantity())
                    .unitPrice(unitPrice)
                    .subtotal(lineSubtotal)
                    .build();

            sale.getItems().add(item);
            subtotal = subtotal.add(lineSubtotal);

            // 5. Deduct stock
            product.setQuantity(product.getQuantity() - itemReq.quantity());
            productRepository.save(product);

            // 6. Log stock movement
            StockMovement movement = StockMovement.builder()
                    .product(product)
                    .user(currentUser)
                    .quantityChange(-itemReq.quantity())
                    .reason(MovementReason.SOLD)
                    .notes("Sold via " + sale.getSaleNumber())
                    .build();
            stockMovementRepository.save(movement);

            // 7. Broadcast WebSocket update
            Map<String, Object> update = new HashMap<>();
            update.put("productId", product.getId());
            update.put("sku", product.getSku());
            update.put("newQuantity", product.getQuantity());
            update.put("delta", -itemReq.quantity());
            update.put("saleNumber", sale.getSaleNumber());
            messagingTemplate.convertAndSend("/topic/stock-updates", update);
        }

        // 8. Calculate totals
        BigDecimal total = subtotal
                .subtract(sale.getDiscount())
                .add(sale.getTax());

        sale.setSubtotal(subtotal);
        sale.setTotalAmount(total);

        // 9. Award loyalty points (1 point per $10 spent)
        if (customer != null) {
            int points = total.divide(BigDecimal.TEN, 0, BigDecimal.ROUND_DOWN).intValue();
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
            customerRepository.save(customer);
        }

        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Transactional
    public SaleResponse refund(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found: " + id));

        if (sale.getStatus() == SaleStatus.REFUNDED) {
            throw new IllegalStateException("Sale already refunded");
        }

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Restore stock for each item
        for (SaleItem item : sale.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);

            StockMovement movement = StockMovement.builder()
                    .product(product)
                    .user(currentUser)
                    .quantityChange(item.getQuantity())
                    .reason(MovementReason.RETURNED)
                    .notes("Refund for sale " + sale.getSaleNumber())
                    .build();
            stockMovementRepository.save(movement);

            Map<String, Object> update = new HashMap<>();
            update.put("productId", product.getId());
            update.put("sku", product.getSku());
            update.put("newQuantity", product.getQuantity());
            update.put("delta", item.getQuantity());
            messagingTemplate.convertAndSend("/topic/stock-updates", update);
        }

        sale.setStatus(SaleStatus.REFUNDED);
        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Transactional
    public void delete(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found: " + id));
        if (sale.getStatus() == SaleStatus.COMPLETED) {
            throw new IllegalStateException("Cannot delete a completed sale – refund it instead");
        }
        saleRepository.deleteById(id);
    }
}
