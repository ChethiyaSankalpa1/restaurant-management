package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String tableId;
    private String tableNumber;
    private LocalDate date;
    private LocalTime time;
    private Integer guests;
    private String status; // Pending, Confirmed, Cancelled, Completed
    private String notes;
    private String branch;
}
