package com.restaurant.repository;

import com.restaurant.model.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface StaffRepository extends MongoRepository<Staff, String> {
    List<Staff> findByBranch(String branch);
    List<Staff> findByStatus(String status);
    List<Staff> findByDepartment(String department);
    Optional<Staff> findByEmployeeNumber(String employeeNumber);
    Optional<Staff> findByEmail(String email);
    long countByStatus(String status);
    long countByBranch(String branch);
}
