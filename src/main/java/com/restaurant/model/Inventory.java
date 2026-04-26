package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory")
public class Inventory {
    @Id
    private String id;
    private String productName;
    private Double quantity;
    private String unit; // kg, liters, pieces, etc.
    private Double minStock;
    private Double maxStock;
    private Double costPrice;
    private String supplierId;
    private String supplierName;
    private LocalDateTime lastUpdated;
    private String branch;
}
