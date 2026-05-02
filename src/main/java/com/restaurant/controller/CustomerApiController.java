package com.restaurant.controller;

import com.restaurant.model.Customer;
import com.restaurant.model.Order;
import com.restaurant.model.OrderItem;
import com.restaurant.repository.CustomerRepository;
import com.restaurant.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerApiController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @PostMapping("/checkout")
    public ResponseEntity<?> processCheckout(@RequestBody CheckoutRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        
        String email = authentication.getName();
        Customer customer = customerRepository.findAll().stream()
                .filter(c -> email.equals(c.getEmail()))
                .findFirst().orElse(null);
                
        String customerName = customer != null ? customer.getName() : email;

        double total = request.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

        Order order = Order.builder()
                .orderNumber("ONL-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .customerName(customerName)
                .customerId(email)
                .type("Delivery") // Default to delivery for online orders
                .status("Pending")
                .paymentStatus("Pending")
                .paymentMethod(request.getPaymentMethod())
                .totalAmount(total)
                .taxAmount(total * 0.1) // 10% tax for example
                .discountAmount(0.0)
                .items(request.getItems())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .source("Online")
                .build();

        orderRepository.save(order);

        if (customer != null) {
            customer.setTotalOrders((customer.getTotalOrders() != null ? customer.getTotalOrders() : 0) + 1);
            customer.setTotalSpent((customer.getTotalSpent() != null ? customer.getTotalSpent() : 0.0) + total);
            customerRepository.save(customer);
        }

        return ResponseEntity.ok(order);
    }

    @Data
    public static class CheckoutRequest {
        private List<OrderItem> items;
        private String paymentMethod;
    }
}
