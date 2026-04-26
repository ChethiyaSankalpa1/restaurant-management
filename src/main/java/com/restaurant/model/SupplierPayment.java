package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "supplierPayments")
public class SupplierPayment {
    @Id
    private String id;
    private String supplierId;
    private String supplierName;
    private Double amount;
    private LocalDate date;
    private String method; // Cash, Bank Transfer, Cheque
    private String notes;
    private String status; // Pending, Paid
    private String branch;
}
