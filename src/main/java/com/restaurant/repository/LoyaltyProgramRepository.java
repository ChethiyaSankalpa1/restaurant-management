package com.restaurant.repository;

import com.restaurant.model.LoyaltyProgram;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface LoyaltyProgramRepository extends MongoRepository<LoyaltyProgram, String> {
    Optional<LoyaltyProgram> findByCustomerId(String customerId);
    List<LoyaltyProgram> findByTier(String tier);
}
