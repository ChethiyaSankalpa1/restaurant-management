package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "loyaltyProgram")
public class LoyaltyProgram {
    @Id
    private String id;
    private String customerId;
    private String customerName;
    private Integer points;
    private String tier; // Bronze, Silver, Gold
    private List<LoyaltyTransaction> transactions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoyaltyTransaction {
        private String type; // EARNED, REDEEMED
        private Integer points;
        private String orderId;
        private String description;
        private LocalDateTime createdAt;
    }
}
