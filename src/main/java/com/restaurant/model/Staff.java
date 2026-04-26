package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "staff")
public class Staff {
    @Id
    private String id;
    private String employeeNumber;
    private String name;
    private String email;
    private String phone;
    private String position;
    private String department;
    private Double salary;
    private LocalDate hireDate;
    private String status; // active, inactive, on_leave
    private String branch;
    private String userId; // linked User account
}
