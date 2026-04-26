package com.restaurant.repository;

import com.restaurant.model.InventoryAlert;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InventoryAlertRepository extends MongoRepository<InventoryAlert, String> {
    List<InventoryAlert> findByStatus(String status);
    List<InventoryAlert> findByProductId(String productId);
    List<InventoryAlert> findByBranch(String branch);
    long countByStatus(String status);
}
