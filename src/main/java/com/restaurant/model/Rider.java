package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "riders")
public class Rider {
    @Id
    private String id;
    private String name;
    private String phone;
    private String vehicleType; // Bicycle, Motorcycle, Car
    private String vehicleNumber;
    private String licenseNumber;
    private Double salary;
    private String status; // active, inactive
    private LocalDateTime createdAt;
}
