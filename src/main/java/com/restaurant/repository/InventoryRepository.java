package com.restaurant.repository;

import com.restaurant.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findByBranch(String branch);
    List<Inventory> findBySupplierId(String supplierId);
    List<Inventory> findByProductNameContainingIgnoreCase(String name);
}
