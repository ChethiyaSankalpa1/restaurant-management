package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    private String accountNumber;
    private String name;
    private String type; // Asset, Liability, Equity, Revenue, Expense
    private String category;
    private Double openingBalance;
    private Double currentBalance;
    private Double totalDebit;
    private Double totalCredit;
    private String status; // active, inactive
    private Boolean isSystem; // system accounts can't be deleted
    private String branch;
}
