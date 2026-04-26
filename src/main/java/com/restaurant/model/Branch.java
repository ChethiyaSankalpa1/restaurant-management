package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "branches")
public class Branch {
    @Id
    private String id;
    private String name;
    private String address;
    private String phone;
    private String managerId;
    private String managerName;
    private String status; // active, inactive
    private String email;
}
