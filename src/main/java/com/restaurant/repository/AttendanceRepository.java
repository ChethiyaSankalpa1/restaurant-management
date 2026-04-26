package com.restaurant.repository;

import com.restaurant.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByEmployeeId(String employeeId);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId, LocalDate start, LocalDate end);
    Optional<Attendance> findByEmployeeIdAndDate(String employeeId, LocalDate date);
    long countByEmployeeIdAndDateBetweenAndStatus(String employeeId, LocalDate start, LocalDate end, String status);
}
