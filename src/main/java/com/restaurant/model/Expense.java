package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expenses")
public class Expense {
    @Id
    private String id;
    private String expenseNumber;
    private String category; // Maintenance, Marketing, Supplies, Rent, Utilities
    private String vendor;
    private Double amount;
    private String paymentMethod;
    private LocalDate date;
    private String status; // Pending, Approved, Rejected
    private String approvedBy;
    private String notes;
    private String branch;
}
