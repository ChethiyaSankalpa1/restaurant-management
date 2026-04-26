package com.restaurant.repository;

import com.restaurant.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByBranch(String branch);
    List<Category> findByOrderBySortOrderAsc();
    List<Category> findByActiveTrue();
}
