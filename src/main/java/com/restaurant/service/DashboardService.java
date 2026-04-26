package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final InventoryAlertRepository alertRepository;
    private final InventoryRepository inventoryRepository;

    public Map<String, Object> getDashboardStats(String period) {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime start = getStartDate(period);
        LocalDateTime end = LocalDateTime.now();

        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        List<Order> completedOrders = orders.stream()
            .filter(o -> "Completed".equals(o.getStatus()))
            .collect(Collectors.toList());

        double totalRevenue = completedOrders.stream()
            .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0)
            .sum();

        long uniqueCustomers = completedOrders.stream()
            .filter(o -> o.getCustomerId() != null)
            .map(Order::getCustomerId).distinct().count();

        double avgOrderValue = completedOrders.isEmpty() ? 0 :
            totalRevenue / completedOrders.size();

        stats.put("totalOrders", orders.size());
        stats.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        stats.put("uniqueCustomers", uniqueCustomers);
        stats.put("avgOrderValue", Math.round(avgOrderValue * 100.0) / 100.0);

        // Status breakdown
        Map<String, Long> statusBreakdown = new HashMap<>();
        for (String s : Arrays.asList("Completed", "Pending", "Preparing", "Ready", "Cancelled")) {
            statusBreakdown.put(s, orders.stream().filter(o -> s.equals(o.getStatus())).count());
        }
        stats.put("statusBreakdown", statusBreakdown);

        // Type breakdown
        Map<String, Long> typeBreakdown = new HashMap<>();
        for (String t : Arrays.asList("DineIn", "Takeaway", "Delivery")) {
            typeBreakdown.put(t, orders.stream().filter(o -> t.equals(o.getType())).count());
        }
        stats.put("typeBreakdown", typeBreakdown);

        // Daily sales trend (last 7 days)
        List<String> labels = new ArrayList<>();
        List<Double> salesData = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            labels.add(day.toString());
            LocalDateTime dayStart = day.atStartOfDay();
            LocalDateTime dayEnd = day.atTime(23, 59, 59);
            double daySales = orderRepository.findByCreatedAtBetween(dayStart, dayEnd).stream()
                .filter(o -> "Completed".equals(o.getStatus()))
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0)
                .sum();
            salesData.add(Math.round(daySales * 100.0) / 100.0);
        }
        stats.put("salesLabels", labels);
        stats.put("salesData", salesData);

        // Recent orders
        List<Order> recentOrders = orderRepository.findAll().stream()
            .sorted((a, b) -> {
                if (a.getCreatedAt() == null && b.getCreatedAt() == null) return 0;
                if (a.getCreatedAt() == null) return 1;
                if (b.getCreatedAt() == null) return -1;
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            })
            .limit(10)
            .collect(Collectors.toList());
        stats.put("recentOrders", recentOrders);

        // Low inventory alerts
        List<InventoryAlert> alerts = alertRepository.findByStatus("active");
        stats.put("inventoryAlerts", alerts);
        stats.put("alertCount", alerts.size());

        return stats;
    }

    private LocalDateTime getStartDate(String period) {
        return switch (period != null ? period : "today") {
            case "week" -> LocalDateTime.now().minusDays(7);
            case "month" -> LocalDateTime.now().minusDays(30);
            default -> LocalDate.now().atStartOfDay();
        };
    }
}
