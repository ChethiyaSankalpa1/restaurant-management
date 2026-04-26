package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionRepository;

    public void recordOrderJournalEntry(Order order) {
        if (order.getTotalAmount() == null) return;
        // Debit: Cash/Bank Account (1001), Credit: Food Sales (4001)
        Account cash = accountRepository.findByAccountNumber("1001").orElse(null);
        Account sales = accountRepository.findByAccountNumber("4001").orElse(null);
        if (cash == null || sales == null) return;

        AccountTransaction tx = AccountTransaction.builder()
            .date(LocalDate.now())
            .description("Order payment received: " + order.getOrderNumber())
            .debitAccountId(cash.getId())
            .debitAccountName(cash.getName())
            .creditAccountId(sales.getId())
            .creditAccountName(sales.getName())
            .amount(order.getTotalAmount())
            .reference(order.getId())
            .type("ORDER")
            .branch(order.getBranch())
            .build();
        transactionRepository.save(tx);

        // Update account balances
        cash.setCurrentBalance((cash.getCurrentBalance() != null ? cash.getCurrentBalance() : 0) + order.getTotalAmount());
        cash.setTotalDebit((cash.getTotalDebit() != null ? cash.getTotalDebit() : 0) + order.getTotalAmount());
        accountRepository.save(cash);

        sales.setCurrentBalance((sales.getCurrentBalance() != null ? sales.getCurrentBalance() : 0) + order.getTotalAmount());
        sales.setTotalCredit((sales.getTotalCredit() != null ? sales.getTotalCredit() : 0) + order.getTotalAmount());
        accountRepository.save(sales);
    }

    public void recordExpenseJournalEntry(Expense expense) {
        Account expenseAcc = accountRepository.findByAccountNumber("5001").orElse(null);
        Account cashAcc = accountRepository.findByAccountNumber("1001").orElse(null);
        if (expenseAcc == null || cashAcc == null) return;

        AccountTransaction tx = AccountTransaction.builder()
            .date(LocalDate.now())
            .description("Expense: " + expense.getCategory() + " - " + expense.getVendor())
            .debitAccountId(expenseAcc.getId())
            .debitAccountName(expenseAcc.getName())
            .creditAccountId(cashAcc.getId())
            .creditAccountName(cashAcc.getName())
            .amount(expense.getAmount())
            .reference(expense.getId())
            .type("EXPENSE")
            .branch(expense.getBranch())
            .build();
        transactionRepository.save(tx);

        expenseAcc.setCurrentBalance((expenseAcc.getCurrentBalance() != null ? expenseAcc.getCurrentBalance() : 0) + expense.getAmount());
        expenseAcc.setTotalDebit((expenseAcc.getTotalDebit() != null ? expenseAcc.getTotalDebit() : 0) + expense.getAmount());
        accountRepository.save(expenseAcc);

        cashAcc.setCurrentBalance((cashAcc.getCurrentBalance() != null ? cashAcc.getCurrentBalance() : 0) - expense.getAmount());
        cashAcc.setTotalCredit((cashAcc.getTotalCredit() != null ? cashAcc.getTotalCredit() : 0) + expense.getAmount());
        accountRepository.save(cashAcc);
    }

    public List<Account> getAllAccounts() { return accountRepository.findAll(); }
    public List<Account> getAccountsByType(String type) { return accountRepository.findByType(type); }
    public Optional<Account> findById(String id) { return accountRepository.findById(id); }
    public Account save(Account account) { return accountRepository.save(account); }
    public List<AccountTransaction> getAllTransactions() { return transactionRepository.findAll(); }
}
