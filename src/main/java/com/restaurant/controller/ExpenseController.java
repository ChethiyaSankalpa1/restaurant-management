package com.restaurant.controller;

import com.restaurant.model.Expense;
import com.restaurant.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public String expensesPage(@RequestParam(required = false) String status, Model model) {
        var expenses = expenseService.getAllExpenses();
        if (status != null && !status.isEmpty())
            expenses = expenses.stream().filter(e -> status.equals(e.getStatus())).toList();

        model.addAttribute("expenses", expenses);
        model.addAttribute("totalAmount", expenseService.getTotalAmount());
        model.addAttribute("pendingCount", expenseService.getAllExpenses().stream().filter(e -> "Pending".equals(e.getStatus())).count());
        model.addAttribute("approvedCount", expenseService.getAllExpenses().stream().filter(e -> "Approved".equals(e.getStatus())).count());
        model.addAttribute("totalCount", expenseService.getAllExpenses().size());
        model.addAttribute("newExpense", new Expense());
        model.addAttribute("filterStatus", status);
        model.addAttribute("pageTitle", "Expenses");
        model.addAttribute("activeMenu", "expenses");
        return "expenses/index";
    }

    @PostMapping("/save")
    public String saveExpense(@ModelAttribute Expense expense, RedirectAttributes ra) {
        expenseService.save(expense);
        ra.addFlashAttribute("success", "Expense recorded!");
        return "redirect:/expenses";
    }

    @PostMapping("/{id}/approve")
    public String approveExpense(@PathVariable String id, Authentication auth, RedirectAttributes ra) {
        expenseService.approve(id, auth.getName());
        ra.addFlashAttribute("success", "Expense approved!");
        return "redirect:/expenses";
    }

    @PostMapping("/delete/{id}")
    public String deleteExpense(@PathVariable String id, RedirectAttributes ra) {
        expenseService.delete(id);
        ra.addFlashAttribute("success", "Expense deleted");
        return "redirect:/expenses";
    }
}
