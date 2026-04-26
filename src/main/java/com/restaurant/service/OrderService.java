package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final LoyaltyService loyaltyService;
    private final AccountService accountService;
    private final CustomerRepository customerRepository;
    private final TaxRepository taxRepository;

    private static final AtomicInteger orderCounter = new AtomicInteger(1000);

    public Order createOrder(Order order) {
        order.setOrderNumber("ORD-" + orderCounter.incrementAndGet());
        order.setStatus("Pending");
        order.setPaymentStatus("Pending");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Calculate tax
        List<Tax> taxes = taxRepository.findByStatus("active");
        double taxRate = taxes.stream().mapToDouble(Tax::getRate).sum() / 100.0;
        double itemsTotal = order.getItems().stream()
            .mapToDouble(i -> i.getSubtotal() != null ? i.getSubtotal() : 0)
            .sum();
        order.setTaxAmount(Math.round(itemsTotal * taxRate * 100.0) / 100.0);
        double loyaltyDiscount = order.getLoyaltyDiscount() != null ? order.getLoyaltyDiscount() : 0;
        order.setTotalAmount(Math.round((itemsTotal + order.getTaxAmount() - loyaltyDiscount) * 100.0) / 100.0);

        // Set item statuses
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setStatus("Pending"));
        }

        Order saved = orderRepository.save(order);

        // Update customer stats if customerId present
        if (order.getCustomerId() != null) {
            customerRepository.findById(order.getCustomerId()).ifPresent(customer -> {
                customer.setTotalOrders((customer.getTotalOrders() != null ? customer.getTotalOrders() : 0) + 1);
                customerRepository.save(customer);
            });
        }

        return saved;
    }

    public Order updateStatus(String orderId, String newStatus, String updatedBy) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        String oldStatus = order.getStatus();
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        if ("Paid".equals(newStatus)) {
            order.setPaymentStatus("Paid");
        }

        Order saved = orderRepository.save(order);

        // Business logic on completion
        if ("Completed".equals(newStatus) && !"Completed".equals(oldStatus)) {
            inventoryService.deductForOrder(saved);
            accountService.recordOrderJournalEntry(saved);
            if (saved.getCustomerId() != null) {
                loyaltyService.awardPoints(saved);
                customerRepository.findById(saved.getCustomerId()).ifPresent(c -> {
                    double spent = c.getTotalSpent() != null ? c.getTotalSpent() : 0;
                    c.setTotalSpent(Math.round((spent + saved.getTotalAmount()) * 100.0) / 100.0);
                    c.setLastOrderDate(java.time.LocalDate.now());
                    customerRepository.save(c);
                });
            }
        }

        return saved;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll().stream()
            .sorted((a, b) -> {
                if (a.getCreatedAt() == null) return 1;
                if (b.getCreatedAt() == null) return -1;
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            })
            .toList();
    }

    public List<Order> getActiveKitchenOrders() {
        return orderRepository.findAll().stream()
            .filter(o -> List.of("Pending", "Preparing", "Ready").contains(o.getStatus()))
            .sorted((a, b) -> {
                if (a.getCreatedAt() == null) return 1;
                if (b.getCreatedAt() == null) return -1;
                return a.getCreatedAt().compareTo(b.getCreatedAt());
            })
            .toList();
    }

    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    public void cancelOrder(String id, String reason) {
        orderRepository.findById(id).ifPresent(order -> {
            order.setStatus("Cancelled");
            order.setNotes((order.getNotes() != null ? order.getNotes() + " | " : "") + "Cancelled: " + reason);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        });
    }

    public long countByStatus(String status) {
        return orderRepository.countByStatus(status);
    }

    public double getTodayRevenue() {
        LocalDateTime start = java.time.LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        return orderRepository.findByCreatedAtBetween(start, end).stream()
            .filter(o -> "Completed".equals(o.getStatus()))
            .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0)
            .sum();
    }
}
