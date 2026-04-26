package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "suppliers")
public class Supplier {
    @Id
    private String id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private List<String> products;
    private String status; // active, inactive
    private String branch;
}
