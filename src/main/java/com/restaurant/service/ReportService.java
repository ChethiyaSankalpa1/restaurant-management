package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderRepository orderRepository;
    private final ExpenseRepository expenseRepository;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayrollRepository payrollRepository;
    private final RiderRepository riderRepository;

    public Map<String, Object> generateReport(String type, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        result.put("reportType", type);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("generatedAt", LocalDateTime.now());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        List<Order> completedOrders = orders.stream().filter(o -> "Completed".equals(o.getStatus())).toList();

        double totalSales = completedOrders.stream().mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0).sum();
        List<Expense> expenses = expenseRepository.findByDateBetween(startDate, endDate);
        double totalExpenses = expenses.stream().mapToDouble(e -> e.getAmount() != null ? e.getAmount() : 0).sum();
        double netProfit = totalSales - totalExpenses;

        result.put("totalSales", Math.round(totalSales * 100.0) / 100.0);
        result.put("totalOrders", orders.size());
        result.put("completedOrders", completedOrders.size());
        result.put("totalExpenses", Math.round(totalExpenses * 100.0) / 100.0);
        result.put("netProfit", Math.round(netProfit * 100.0) / 100.0);
        result.put("orders", completedOrders);
        result.put("expenses", expenses);

        switch (type) {
            case "PROFIT_LOSS" -> result.put("title", "Profit & Loss Report");
            case "SALES" -> {
                result.put("title", "Sales Report");
                // Group by day
                Map<String, Double> dailySales = new LinkedHashMap<>();
                completedOrders.forEach(o -> {
                    String day = o.getCreatedAt().toLocalDate().toString();
                    dailySales.merge(day, o.getTotalAmount() != null ? o.getTotalAmount() : 0, Double::sum);
                });
                result.put("dailySales", dailySales);
            }
            case "INVENTORY" -> {
                result.put("title", "Inventory Report");
                result.put("inventory", inventoryRepository.findAll());
            }
            case "CUSTOMER" -> {
                result.put("title", "Customer Report");
                result.put("customers", customerRepository.findAll());
            }
            case "PAYROLL" -> {
                result.put("title", "Payroll Report");
                result.put("payrolls", payrollRepository.findAll());
            }
            case "ATTENDANCE" -> {
                result.put("title", "Attendance Report");
                result.put("attendance", attendanceRepository.findByEmployeeIdAndDateBetween("", startDate, endDate));
            }
            case "EXPENSE" -> result.put("title", "Expense Report");
            case "RIDER_PERFORMANCE" -> {
                result.put("title", "Rider Performance Report");
                result.put("riders", riderRepository.findAll());
                long deliveries = completedOrders.stream().filter(o -> "Delivery".equals(o.getType())).count();
                result.put("totalDeliveries", deliveries);
            }
            case "TRIAL_BALANCE" -> result.put("title", "Trial Balance");
            case "BALANCE_SHEET" -> result.put("title", "Balance Sheet");
            default -> result.put("title", type.replace("_", " "));
        }

        return result;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime start = LocalDate.now().minusDays(30).atStartOfDay();
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, LocalDateTime.now());
        List<Order> completed = orders.stream().filter(o -> "Completed".equals(o.getStatus())).toList();
        double sales = completed.stream().mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0).sum();
        double expenses = expenseRepository.findAll().stream().mapToDouble(e -> e.getAmount() != null ? e.getAmount() : 0).sum();

        stats.put("totalSales", Math.round(sales * 100.0) / 100.0);
        stats.put("totalOrders", completed.size());
        stats.put("totalExpenses", Math.round(expenses * 100.0) / 100.0);
        stats.put("netProfit", Math.round((sales - expenses) * 100.0) / 100.0);

        List<String> labels = new ArrayList<>();
        List<Double> salesData = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            labels.add(day.toString());
            LocalDateTime ds = day.atStartOfDay(), de = day.atTime(23, 59, 59);
            double s = orderRepository.findByCreatedAtBetween(ds, de).stream()
                .filter(o -> "Completed".equals(o.getStatus()))
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0).sum();
            salesData.add(Math.round(s * 100.0) / 100.0);
        }
        stats.put("salesLabels", labels);
        stats.put("salesData", salesData);
        return stats;
    }
}
