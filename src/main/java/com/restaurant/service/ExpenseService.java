package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final AccountService accountService;

    private static final AtomicInteger counter = new AtomicInteger(100);

    public List<Expense> getAllExpenses() { return expenseRepository.findAll(); }
    public Optional<Expense> findById(String id) { return expenseRepository.findById(id); }

    public Expense save(Expense expense) {
        if (expense.getExpenseNumber() == null) {
            expense.setExpenseNumber("EXP-" + counter.incrementAndGet());
        }
        if (expense.getDate() == null) expense.setDate(LocalDate.now());
        if (expense.getStatus() == null) expense.setStatus("Pending");
        return expenseRepository.save(expense);
    }

    public Expense approve(String id, String approvedBy) {
        Expense e = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not found"));
        e.setStatus("Approved");
        e.setApprovedBy(approvedBy);
        Expense saved = expenseRepository.save(e);
        accountService.recordExpenseJournalEntry(saved);
        return saved;
    }

    public void delete(String id) { expenseRepository.deleteById(id); }

    public double getTotalAmount() {
        return expenseRepository.findAll().stream()
            .mapToDouble(e -> e.getAmount() != null ? e.getAmount() : 0).sum();
    }
}
