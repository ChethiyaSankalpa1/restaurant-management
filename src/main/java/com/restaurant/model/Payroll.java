package com.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payroll")
public class Payroll {
    @Id
    private String id;
    private String employeeId;
    private String employeeName;
    private String employeePosition;
    private Integer month;
    private Integer year;
    private Double basicSalary;
    private Double allowances;
    private Double deductions;
    private Double grossSalary;
    private Double netPay;
    private Integer workingDays;
    private Integer presentDays;
    private String status; // Pending, Approved, Paid
    private String approvedBy;
    private String branch;
}
