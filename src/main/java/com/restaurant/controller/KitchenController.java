package com.restaurant.controller;

import com.restaurant.model.Order;
import com.restaurant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kitchen")
public class KitchenController {

    private final OrderService orderService;

    @GetMapping
    public String kitchenPage(@RequestParam(defaultValue = "all") String filter, Model model) {
        List<Order> orders = orderService.getActiveKitchenOrders();
        if ("online".equals(filter))
            orders = orders.stream().filter(o -> "Online".equals(o.getSource())).toList();
        else if ("physical".equals(filter))
            orders = orders.stream().filter(o -> "Physical".equals(o.getSource())).toList();
        else if ("complete".equals(filter))
            orders = orderService.getAllOrders().stream().filter(o -> "Ready".equals(o.getStatus())).toList();

        long pending = orders.stream().filter(o -> "Pending".equals(o.getStatus())).count();
        long preparing = orders.stream().filter(o -> "Preparing".equals(o.getStatus())).count();
        long ready = orders.stream().filter(o -> "Ready".equals(o.getStatus())).count();
        long total = orders.stream().filter(o -> !o.getStatus().equals("Cancelled")).count();
        int onTimeRate = total > 0 ? (int) ((ready + preparing) * 100 / total) : 100;

        model.addAttribute("orders", orders);
        model.addAttribute("pendingCount", pending);
        model.addAttribute("preparingCount", preparing);
        model.addAttribute("readyCount", ready);
        model.addAttribute("onTimeRate", onTimeRate);
        model.addAttribute("activeFilter", filter);
        model.addAttribute("pageTitle", "Kitchen Management");
        model.addAttribute("activeMenu", "kitchen");
        return "kitchen/index";
    }

    @GetMapping("/api/orders")
    @ResponseBody
    public List<Order> getKitchenOrders() {
        return orderService.getActiveKitchenOrders();
    }

    @PostMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable String id, @RequestParam String status) {
        try {
            Order updated = orderService.updateStatus(id, status, "Kitchen");
            return ResponseEntity.ok(Map.of("success", true, "status", updated.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
