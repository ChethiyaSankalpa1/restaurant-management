package com.restaurant.controller;

import com.restaurant.model.Order;
import com.restaurant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String ordersPage(@RequestParam(required = false) String status,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) String source,
                              Model model) {
        List<Order> orders = orderService.getAllOrders();
        if (status != null && !status.isEmpty())
            orders = orders.stream().filter(o -> status.equals(o.getStatus())).toList();
        if (type != null && !type.isEmpty())
            orders = orders.stream().filter(o -> type.equals(o.getType())).toList();
        if (source != null && !source.isEmpty())
            orders = orders.stream().filter(o -> source.equals(o.getSource())).toList();

        model.addAttribute("orders", orders);
        model.addAttribute("pendingCount", orderService.countByStatus("Pending"));
        model.addAttribute("preparingCount", orderService.countByStatus("Preparing"));
        model.addAttribute("readyCount", orderService.countByStatus("Ready"));
        model.addAttribute("todayRevenue", orderService.getTodayRevenue());
        model.addAttribute("totalCount", orders.size());
        model.addAttribute("filterStatus", status);
        model.addAttribute("filterType", type);
        model.addAttribute("filterSource", source);
        model.addAttribute("pageTitle", "Order Management");
        model.addAttribute("activeMenu", "orders");
        return "orders/index";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable String id, Model model) {
        Order order = orderService.findById(id)
            .orElseThrow(() -> new com.restaurant.exception.ResourceNotFoundException("Order", id));
        model.addAttribute("order", order);
        model.addAttribute("pageTitle", "Order Details");
        model.addAttribute("activeMenu", "orders");
        return "orders/detail";
    }

    @PostMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable String id,
                                                              @RequestParam String status,
                                                              @RequestParam(required = false) String updatedBy) {
        try {
            Order updated = orderService.updateStatus(id, status, updatedBy);
            return ResponseEntity.ok(Map.of("success", true, "status", updated.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable String id, @RequestParam String reason, RedirectAttributes ra) {
        orderService.cancelOrder(id, reason);
        ra.addFlashAttribute("success", "Order cancelled successfully");
        return "redirect:/orders";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public List<Order> getOrdersApi() {
        return orderService.getAllOrders();
    }
}
