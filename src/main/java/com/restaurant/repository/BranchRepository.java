package com.restaurant.repository;

import com.restaurant.model.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BranchRepository extends MongoRepository<Branch, String> {
    List<Branch> findByStatus(String status);
}
