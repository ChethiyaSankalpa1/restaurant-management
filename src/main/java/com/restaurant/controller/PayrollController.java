package com.restaurant.controller;

import com.restaurant.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/staff/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    @GetMapping
    public String payrollPage(Model model) {
        var payrolls = payrollService.getAllPayroll();
        model.addAttribute("payrolls", payrolls);
        model.addAttribute("totalPayrolls", payrolls.size());
        model.addAttribute("paidCount", payrolls.stream().filter(p -> "Paid".equals(p.getStatus())).count());
        model.addAttribute("pendingCount", payrolls.stream().filter(p -> "Pending".equals(p.getStatus())).count());
        model.addAttribute("totalAmount", payrollService.getTotalNetPay(payrolls));
        model.addAttribute("currentMonth", LocalDate.now().getMonthValue());
        model.addAttribute("currentYear", LocalDate.now().getYear());
        model.addAttribute("pageTitle", "Payroll Management");
        model.addAttribute("activeMenu", "staff");
        return "staff/payroll";
    }

    @PostMapping("/generate")
    public String generatePayroll(@RequestParam int month, @RequestParam int year, RedirectAttributes ra) {
        payrollService.generateMonthlyPayroll(month, year);
        ra.addFlashAttribute("success", "Payroll generated for " + month + "/" + year);
        return "redirect:/staff/payroll";
    }

    @PostMapping("/{id}/approve")
    public String approvePayroll(@PathVariable String id, Authentication auth, RedirectAttributes ra) {
        payrollService.approve(id, auth.getName());
        ra.addFlashAttribute("success", "Payroll approved!");
        return "redirect:/staff/payroll";
    }

    @PostMapping("/{id}/paid")
    public String markPaid(@PathVariable String id, RedirectAttributes ra) {
        payrollService.markPaid(id);
        ra.addFlashAttribute("success", "Payroll marked as Paid!");
        return "redirect:/staff/payroll";
    }

    @PostMapping("/{id}/delete")
    public String deletePayroll(@PathVariable String id, RedirectAttributes ra) {
        payrollService.delete(id);
        ra.addFlashAttribute("success", "Payroll record deleted");
        return "redirect:/staff/payroll";
    }
}
