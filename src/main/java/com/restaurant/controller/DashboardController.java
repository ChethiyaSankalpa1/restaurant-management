package com.restaurant.controller;

import com.restaurant.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DashboardController {

    private final DashboardService dashboardService;
    private final com.restaurant.repository.CategoryRepository categoryRepository;
    private final com.restaurant.repository.MenuItemRepository menuItemRepository;
    private final com.restaurant.repository.CustomerRepository customerRepository;
    private final com.restaurant.repository.TableRepository tableRepository;
    private final com.restaurant.repository.StaffRepository staffRepository;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "today") String period, Model model) {
        Map<String, Object> stats = dashboardService.getDashboardStats(period);
        model.addAllAttributes(stats);
        model.addAttribute("activePeriod", period);
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activeMenu", "dashboard");
        return "dashboard/index";
    }

    @GetMapping("/dashboard/stats")
    @ResponseBody
    public Map<String, Object> getDashboardStats(@RequestParam(defaultValue = "today") String period) {
        return dashboardService.getDashboardStats(period);
    }

    @GetMapping("/debug/counts")
    @ResponseBody
    public Map<String, Long> getCounts() {
        return Map.of(
            "categories", categoryRepository.count(),
            "menuItems", menuItemRepository.count(),
            "customers", customerRepository.count(),
            "tables", tableRepository.count(),
            "staff", staffRepository.count()
        );
    }
}
