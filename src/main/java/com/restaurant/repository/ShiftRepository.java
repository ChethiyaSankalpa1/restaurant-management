package com.restaurant.repository;

import com.restaurant.model.Shift;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends MongoRepository<Shift, String> {
    List<Shift> findByEmployeeId(String employeeId);
    List<Shift> findByDate(LocalDate date);
    List<Shift> findByDateBetween(LocalDate start, LocalDate end);
    List<Shift> findByBranch(String branch);
    long countByEmployeeIdAndDateBetween(String employeeId, LocalDate start, LocalDate end);
}
