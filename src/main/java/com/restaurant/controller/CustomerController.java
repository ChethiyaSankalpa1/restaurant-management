package com.restaurant.controller;

import com.restaurant.model.Customer;
import com.restaurant.service.CustomerService;
import com.restaurant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

    @GetMapping
    public String customersPage(@RequestParam(required = false) String search, Model model) {
        var customers = (search != null && !search.isEmpty()) ?
            customerService.search(search) : customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        model.addAttribute("totalCustomers", customerService.getAllCustomers().size());
        model.addAttribute("activeCustomers", customerService.countByStatus("active"));
        model.addAttribute("totalRevenue", customerService.getTotalRevenue());
        model.addAttribute("avgOrderValue", customerService.getAvgOrderValue());
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Customer Management");
        model.addAttribute("activeMenu", "customers");
        return "customers/index";
    }

    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable String id, Model model) {
        Customer customer = customerService.findById(id)
            .orElseThrow(() -> new com.restaurant.exception.ResourceNotFoundException("Customer", id));
        var orders = orderService.getAllOrders().stream()
            .filter(o -> id.equals(o.getCustomerId())).toList();
        model.addAttribute("customer", customer);
        model.addAttribute("orders", orders);
        model.addAttribute("pageTitle", "Customer Profile");
        model.addAttribute("activeMenu", "customers");
        return "customers/profile";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute Customer customer, RedirectAttributes ra) {
        if (customer.getStatus() == null) customer.setStatus("active");
        if (customer.getTotalOrders() == null) customer.setTotalOrders(0);
        if (customer.getTotalSpent() == null) customer.setTotalSpent(0.0);
        if (customer.getLoyaltyPoints() == null) customer.setLoyaltyPoints(0);
        customerService.save(customer);
        ra.addFlashAttribute("success", "Customer saved!");
        return "redirect:/customers";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable String id, RedirectAttributes ra) {
        customerService.delete(id);
        ra.addFlashAttribute("success", "Customer deleted");
        return "redirect:/customers";
    }
}
