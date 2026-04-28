package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "settings")
public class SystemSetting {
    @Id
    private String id;
    private String key;
    private String value;
}
