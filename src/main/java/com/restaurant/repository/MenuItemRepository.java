package com.restaurant.repository;

import com.restaurant.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    List<MenuItem> findByCategoryId(String categoryId);
    List<MenuItem> findByBranch(String branch);
    List<MenuItem> findByAvailable(Boolean available);
    List<MenuItem> findByBranchAndAvailable(String branch, Boolean available);
    List<MenuItem> findByNameContainingIgnoreCase(String name);
}
