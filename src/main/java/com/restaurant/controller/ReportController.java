package com.restaurant.controller;

import com.restaurant.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public String reportsPage(Model model) {
        Map<String, Object> stats = reportService.getDashboardStats();
        model.addAllAttributes(stats);
        model.addAttribute("pageTitle", "Reports Dashboard");
        model.addAttribute("activeMenu", "reports");
        return "reports/index";
    }

    @GetMapping("/generate")
    public String generateReport(
        @RequestParam String type,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Model model) {
        Map<String, Object> reportData = reportService.generateReport(type, startDate, endDate);
        model.addAllAttributes(reportData);
        model.addAttribute("pageTitle", reportData.get("title"));
        model.addAttribute("activeMenu", "reports");
        return "reports/generated";
    }
}
