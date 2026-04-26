package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventoryAlerts")
public class InventoryAlert {
    @Id
    private String id;
    private String productId;
    private String productName;
    private Double currentStock;
    private Double minStock;
    private LocalDateTime alertDate;
    private String status; // active, resolved
    private String branch;
}
