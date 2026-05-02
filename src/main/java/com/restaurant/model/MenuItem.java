package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "menuItems")
public class MenuItem {
    @Id
    private String id;
    private String name;
    @Indexed
    private String categoryId;
    private String categoryName;
    private Double price;
    private String imageUrl;
    private String description;
    @Indexed
    private Boolean available;
    private String branch;
    private String inventoryProductId; // Link to inventory for auto-deduction
    private Integer preparationTime; // minutes
}
