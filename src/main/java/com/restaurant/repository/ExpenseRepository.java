package com.restaurant.repository;

import com.restaurant.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByStatus(String status);
    List<Expense> findByBranch(String branch);
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);
    List<Expense> findByCategory(String category);
    long countByStatus(String status);
}
