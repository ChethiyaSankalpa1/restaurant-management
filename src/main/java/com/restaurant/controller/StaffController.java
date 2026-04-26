package com.restaurant.controller;

import com.restaurant.model.*;
import com.restaurant.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public String staffPage(Model model) {
        model.addAttribute("staffList", staffService.getAllStaff());
        model.addAttribute("recentShifts", staffService.getRecentShifts());
        model.addAttribute("totalEmployees", staffService.getAllStaff().size());
        model.addAttribute("activeEmployees", staffService.countByStatus("active"));
        model.addAttribute("totalShifts", staffService.getRecentShifts().size());
        model.addAttribute("totalSalary", staffService.getTotalSalary());
        model.addAttribute("newStaff", new Staff());
        model.addAttribute("newShift", new Shift());
        model.addAttribute("pageTitle", "Staff Management");
        model.addAttribute("activeMenu", "staff");
        return "staff/index";
    }

    @PostMapping("/save")
    public String saveStaff(@ModelAttribute Staff staff, RedirectAttributes ra) {
        if (staff.getStatus() == null) staff.setStatus("active");
        if (staff.getEmployeeNumber() == null || staff.getEmployeeNumber().isEmpty()) {
            staff.setEmployeeNumber("EMP-" + System.currentTimeMillis());
        }
        staffService.save(staff);
        ra.addFlashAttribute("success", "Employee saved!");
        return "redirect:/staff";
    }

    @PostMapping("/delete/{id}")
    public String deleteStaff(@PathVariable String id, RedirectAttributes ra) {
        staffService.delete(id);
        ra.addFlashAttribute("success", "Employee deleted");
        return "redirect:/staff";
    }

    @PostMapping("/shift/save")
    public String saveShift(@ModelAttribute Shift shift, RedirectAttributes ra) {
        staffService.saveShift(shift);
        ra.addFlashAttribute("success", "Shift saved!");
        return "redirect:/staff";
    }
}
