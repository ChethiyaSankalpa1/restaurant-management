package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "taxes")
public class Tax {
    @Id
    private String id;
    private String name;
    private Double rate; // percentage, e.g. 10.0 for 10%
    private String type; // VAT, GST, Service
    private List<String> appliesTo; // all, dine-in, takeaway, delivery
    private String status; // active, inactive
}
