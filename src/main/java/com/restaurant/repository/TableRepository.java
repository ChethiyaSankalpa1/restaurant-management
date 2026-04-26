package com.restaurant.repository;

import com.restaurant.model.RestaurantTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends MongoRepository<RestaurantTable, String> {
    List<RestaurantTable> findByBranch(String branch);
    List<RestaurantTable> findByStatus(String status);
    List<RestaurantTable> findByBranchAndStatus(String branch, String status);
    Optional<RestaurantTable> findByTableNumber(String tableNumber);
    long countByStatus(String status);
    long countByBranchAndStatus(String branch, String status);
}
