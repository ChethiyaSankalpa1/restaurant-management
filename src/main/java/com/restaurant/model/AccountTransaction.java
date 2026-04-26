package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class AccountTransaction {
    @Id
    private String id;
    private LocalDate date;
    private String description;
    private String debitAccountId;
    private String debitAccountName;
    private String creditAccountId;
    private String creditAccountName;
    private Double amount;
    private String reference; // order ID, expense ID, etc.
    private String type; // ORDER, EXPENSE, PAYROLL, SUPPLIER_PAYMENT
    private String branch;
}
