package com.restaurant.repository;

import com.restaurant.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
    List<Account> findByType(String type);
    List<Account> findByStatus(String status);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByNameContainingIgnoreCase(String name);
}
