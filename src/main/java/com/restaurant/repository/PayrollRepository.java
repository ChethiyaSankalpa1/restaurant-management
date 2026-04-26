package com.restaurant.repository;

import com.restaurant.model.Payroll;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends MongoRepository<Payroll, String> {
    List<Payroll> findByMonthAndYear(Integer month, Integer year);
    List<Payroll> findByEmployeeId(String employeeId);
    List<Payroll> findByStatus(String status);
    Optional<Payroll> findByEmployeeIdAndMonthAndYear(String employeeId, Integer month, Integer year);
    long countByStatus(String status);
}
