package com.restaurant.repository;

import com.restaurant.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    List<User> findByBranch(String branch);
    List<User> findByStatus(String status);
    boolean existsByEmail(String email);
}
