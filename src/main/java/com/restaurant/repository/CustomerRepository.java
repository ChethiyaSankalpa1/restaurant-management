package com.restaurant.repository;

import com.restaurant.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);
    List<Customer> findByStatus(String status);
    List<Customer> findByNameContainingIgnoreCase(String name);
    long countByStatus(String status);
}
