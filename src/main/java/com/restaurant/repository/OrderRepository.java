package com.restaurant.repository;

import com.restaurant.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByStatus(String status);
    List<Order> findByBranch(String branch);
    List<Order> findByType(String type);
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Order> findByStatusAndBranch(String status, String branch);
    List<Order> findByCustomerId(String customerId);
    List<Order> findByBranchAndCreatedAtBetween(String branch, LocalDateTime start, LocalDateTime end);
    long countByStatus(String status);
    long countByStatusAndCreatedAtBetween(String status, LocalDateTime start, LocalDateTime end);
}
