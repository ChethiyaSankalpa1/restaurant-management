package com.restaurant.controller;

import com.restaurant.model.Attendance;
import com.restaurant.service.AttendanceService;
import com.restaurant.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StaffService staffService;

    @GetMapping
    public String attendancePage(@RequestParam(required = false) String employeeId,
                                  @RequestParam(defaultValue = "0") int month,
                                  @RequestParam(defaultValue = "0") int year, Model model) {
        if (month == 0) month = LocalDate.now().getMonthValue();
        if (year == 0) year = LocalDate.now().getYear();

        model.addAttribute("staffList", staffService.getAllStaff());
        model.addAttribute("selectedEmployee", employeeId);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("allAttendance", attendanceService.getAllAttendance());

        if (employeeId != null && !employeeId.isEmpty()) {
            model.addAttribute("monthlyAttendance", attendanceService.getMonthlyAttendance(employeeId, month, year));
        }
        model.addAttribute("pageTitle", "Attendance Management");
        model.addAttribute("activeMenu", "attendance");
        return "attendance/index";
    }

    @PostMapping("/checkin/{employeeId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkIn(@PathVariable String employeeId) {
        try {
            Attendance att = attendanceService.checkIn(employeeId);
            return ResponseEntity.ok(Map.of("success", true, "checkIn", att.getCheckIn().toString(),
                "status", att.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/checkout/{employeeId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkOut(@PathVariable String employeeId) {
        try {
            Attendance att = attendanceService.checkOut(employeeId);
            return ResponseEntity.ok(Map.of("success", true, "checkOut", att.getCheckOut().toString(),
                "hours", att.getHours()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/save")
    public String saveAttendance(@ModelAttribute Attendance att, RedirectAttributes ra) {
        attendanceService.save(att);
        ra.addFlashAttribute("success", "Attendance saved!");
        return "redirect:/attendance";
    }
}
