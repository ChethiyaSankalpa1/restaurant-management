package com.restaurant.repository;

import com.restaurant.model.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SupplierRepository extends MongoRepository<Supplier, String> {
    List<Supplier> findByStatus(String status);
    List<Supplier> findByBranch(String branch);
}
