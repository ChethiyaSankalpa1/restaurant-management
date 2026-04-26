package com.restaurant.controller;

import com.restaurant.service.LoyaltyService;
import com.restaurant.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;
    private final CustomerService customerService;

    @GetMapping
    public String loyaltyPage(Model model) {
        model.addAttribute("loyaltyAccounts", loyaltyService.getAllLoyaltyAccounts());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("bronzeCount", loyaltyService.getAllLoyaltyAccounts().stream().filter(l -> "Bronze".equals(l.getTier())).count());
        model.addAttribute("silverCount", loyaltyService.getAllLoyaltyAccounts().stream().filter(l -> "Silver".equals(l.getTier())).count());
        model.addAttribute("goldCount", loyaltyService.getAllLoyaltyAccounts().stream().filter(l -> "Gold".equals(l.getTier())).count());
        model.addAttribute("pageTitle", "Loyalty Program");
        model.addAttribute("activeMenu", "loyalty");
        return "loyalty/index";
    }
}
