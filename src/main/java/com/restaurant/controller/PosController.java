package com.restaurant.controller;

import com.restaurant.model.*;
import com.restaurant.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pos")
public class PosController {

    private final MenuService menuService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final TableService tableService;
    private final StaffService staffService;
    private final RiderService riderService;
    private final TaxController taxHelper;

    @GetMapping
    public String posPage(Model model) {
        List<Category> categories = menuService.getAllCategories();
        List<MenuItem> items = menuService.getAvailableItems();
        List<Customer> customers = customerService.getAllCustomers();
        List<RestaurantTable> tables = tableService.getAllTables().stream()
            .filter(t -> "Available".equals(t.getStatus())).toList();
        List<Staff> staff = staffService.getAllStaff().stream()
            .filter(s -> "active".equals(s.getStatus())).toList();

        System.out.println("POS Loading: Cats=" + categories.size() + ", Items=" + items.size() + 
            ", Custs=" + customers.size() + ", Tables=" + tables.size() + ", Staff=" + staff.size());

        model.addAttribute("categories", categories);
        model.addAttribute("menuItems", items);
        model.addAttribute("customers", customers);
        model.addAttribute("tables", tables);
        model.addAttribute("waiters", staff);
        model.addAttribute("riders", riderService.getActiveRiders());
        model.addAttribute("pageTitle", "POS");
        model.addAttribute("activeMenu", "pos");
        return "pos/index";
    }

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Order order) {
        try {
            Order saved = orderService.createOrder(order);
            return ResponseEntity.ok(Map.of("success", true, "orderId", saved.getId(),
                "orderNumber", saved.getOrderNumber()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/menu/items")
    @ResponseBody
    public List<MenuItem> getMenuItems(@RequestParam(required = false) String categoryId,
                                        @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) return menuService.searchMenuItems(search);
        if (categoryId != null && !categoryId.isEmpty()) return menuService.getByCategory(categoryId);
        return menuService.getAvailableItems();
    }
}
