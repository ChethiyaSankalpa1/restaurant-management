package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    @Indexed(unique = true)
    private String orderNumber;
    private String customerName;
    @Indexed
    private String customerId;
    private String type; // DineIn, Takeaway, Delivery
    private String source; // Online, Physical
    private List<OrderItem> items;
    @Indexed
    private String status; // Pending, Preparing, Ready, Completed, Cancelled
    private String paymentStatus; // Pending, Paid
    private String paymentMethod; // Cash, Card, BankTransfer
    private Double totalAmount;
    private Double taxAmount;
    private Double discountAmount;
    private Double loyaltyDiscount;
    private String tableId;
    private String tableNumber;
    private String waiterId;
    private String waiterName;
    private String riderId;
    private String riderName;
    private String branch;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
