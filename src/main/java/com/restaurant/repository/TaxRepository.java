package com.restaurant.repository;

import com.restaurant.model.Tax;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TaxRepository extends MongoRepository<Tax, String> {
    List<Tax> findByStatus(String status);
}
