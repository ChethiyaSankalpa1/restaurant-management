package com.restaurant.model;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String menuItemId;
    private String name;
    private Integer quantity;
    private Double price;
    private Double subtotal;
    private String status; // Pending, Preparing, Ready
    private String notes;
    private List<String> modifiers;
}
