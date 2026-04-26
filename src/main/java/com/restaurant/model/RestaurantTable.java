package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tables")
public class RestaurantTable {
    @Id
    private String id;
    private String tableNumber;
    private Integer capacity;
    private String location; // Indoor, Outdoor, VIP
    private String status; // Available, Occupied, Reserved
    private String branch;
    private String qrCode; // Base64 encoded QR code image
    private String qrUrl;  // URL encoded in QR
}
