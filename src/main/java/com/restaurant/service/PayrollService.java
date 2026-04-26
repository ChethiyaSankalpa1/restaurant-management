package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final StaffRepository staffRepository;
    private final AttendanceRepository attendanceRepository;

    public void generateMonthlyPayroll(int month, int year) {
        List<Staff> activeStaff = staffRepository.findByStatus("active");
        YearMonth ym = YearMonth.of(year, month);
        int workingDaysInMonth = 26; // standard working days

        for (Staff emp : activeStaff) {
            // Check if payroll already generated
            if (payrollRepository.findByEmployeeIdAndMonthAndYear(emp.getId(), month, year).isPresent()) {
                continue;
            }

            LocalDate startDate = ym.atDay(1);
            LocalDate endDate = ym.atEndOfMonth();

            long presentDays = attendanceRepository
                .countByEmployeeIdAndDateBetweenAndStatus(emp.getId(), startDate, endDate, "Present");
            long lateDays = attendanceRepository
                .countByEmployeeIdAndDateBetweenAndStatus(emp.getId(), startDate, endDate, "Late");

            double basicSalary = emp.getSalary() != null ? emp.getSalary() : 0;
            double dailyRate = basicSalary / workingDaysInMonth;
            double earnedSalary = dailyRate * (presentDays + lateDays * 0.5);
            double allowances = basicSalary * 0.10; // 10% allowance
            double deductions = basicSalary - earnedSalary + (basicSalary * 0.02); // EPF/etc
            double grossSalary = earnedSalary + allowances;
            double netPay = grossSalary - deductions;

            Payroll payroll = Payroll.builder()
                .employeeId(emp.getId())
                .employeeName(emp.getName())
                .employeePosition(emp.getPosition())
                .month(month)
                .year(year)
                .basicSalary(Math.round(basicSalary * 100.0) / 100.0)
                .allowances(Math.round(allowances * 100.0) / 100.0)
                .deductions(Math.round(deductions * 100.0) / 100.0)
                .grossSalary(Math.round(grossSalary * 100.0) / 100.0)
                .netPay(Math.round(netPay * 100.0) / 100.0)
                .workingDays(workingDaysInMonth)
                .presentDays((int) presentDays)
                .status("Pending")
                .branch(emp.getBranch())
                .build();

            payrollRepository.save(payroll);
        }
    }

    public List<Payroll> getAllPayroll() { return payrollRepository.findAll(); }
    public Optional<Payroll> findById(String id) { return payrollRepository.findById(id); }

    public Payroll approve(String id, String approverName) {
        Payroll p = payrollRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payroll not found"));
        p.setStatus("Approved");
        p.setApprovedBy(approverName);
        return payrollRepository.save(p);
    }

    public Payroll markPaid(String id) {
        Payroll p = payrollRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payroll not found"));
        p.setStatus("Paid");
        return payrollRepository.save(p);
    }

    public void delete(String id) { payrollRepository.deleteById(id); }

    public double getTotalNetPay(List<Payroll> payrolls) {
        return payrolls.stream().mapToDouble(p -> p.getNetPay() != null ? p.getNetPay() : 0).sum();
    }
}
