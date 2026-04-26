package com.restaurant.controller;

import com.restaurant.model.Account;
import com.restaurant.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/chart")
    public String chartOfAccounts(@RequestParam(required = false) String type, Model model) {
        var accounts = type != null && !type.isEmpty() ?
            accountService.getAccountsByType(type) : accountService.getAllAccounts();

        Map<String, List<Account>> grouped = new LinkedHashMap<>();
        for (String t : Arrays.asList("Asset", "Liability", "Equity", "Revenue", "Expense")) {
            String ft = t;
            grouped.put(t, accounts.stream().filter(a -> ft.equals(a.getType())).toList());
        }

        model.addAttribute("grouped", grouped);
        model.addAttribute("filterType", type);
        model.addAttribute("newAccount", new Account());
        model.addAttribute("transactions", accountService.getAllTransactions());
        model.addAttribute("pageTitle", "Chart of Accounts");
        model.addAttribute("activeMenu", "accounts");
        return "accounts/chart";
    }

    @PostMapping("/save")
    public String saveAccount(@ModelAttribute Account account, RedirectAttributes ra) {
        if (account.getStatus() == null) account.setStatus("active");
        if (account.getTotalDebit() == null) account.setTotalDebit(0.0);
        if (account.getTotalCredit() == null) account.setTotalCredit(0.0);
        accountService.save(account);
        ra.addFlashAttribute("success", "Account saved!");
        return "redirect:/accounts/chart";
    }
}
