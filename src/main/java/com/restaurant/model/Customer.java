package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Integer totalOrders;
    private Double totalSpent;
    private Integer loyaltyPoints;
    private String status; // active, inactive
    private LocalDate lastOrderDate;
    private String branch;
}
