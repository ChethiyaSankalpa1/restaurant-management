package com.restaurant.repository;

import com.restaurant.model.AccountTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface AccountTransactionRepository extends MongoRepository<AccountTransaction, String> {
    List<AccountTransaction> findByBranch(String branch);
    List<AccountTransaction> findByDateBetween(LocalDate start, LocalDate end);
    List<AccountTransaction> findByDebitAccountId(String accountId);
    List<AccountTransaction> findByCreditAccountId(String accountId);
    List<AccountTransaction> findByReference(String reference);
}
