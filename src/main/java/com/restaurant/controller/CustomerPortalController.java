package com.restaurant.controller;

import com.restaurant.model.Customer;
import com.restaurant.model.Order;
import com.restaurant.model.User;
import com.restaurant.repository.CustomerRepository;
import com.restaurant.repository.OrderRepository;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.MenuService;
import com.restaurant.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerPortalController {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final SettingService settingService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("currencySymbol", settingService.getSetting("app.currency-symbol", "Rs"));
        model.addAttribute("restaurantName", settingService.getSetting("app.restaurant-name", "RestaurantPro"));
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "customer/register";
    }

    @PostMapping("/register")
    public String registerCustomer(@RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam String phone,
                                   @RequestParam String password,
                                   RedirectAttributes redirectAttributes) {
        if (userRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email already exists!");
            return "redirect:/customer/register";
        }

        // Create User for login
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role("CUSTOMER")
                .status("active")
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        // Create Customer profile
        Customer customer = Customer.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .status("active")
                .totalOrders(0)
                .totalSpent(0.0)
                .loyaltyPoints(0)
                .build();
        customerRepository.save(customer);

        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/menu")
    public String showMenu(Model model, @RequestParam(required = false) String categoryId) {
        model.addAttribute("categories", menuService.getAllCategories());
        if (categoryId != null && !categoryId.isEmpty()) {
            model.addAttribute("menuItems", menuService.getByCategory(categoryId));
            model.addAttribute("activeCategory", categoryId);
        } else {
            model.addAttribute("menuItems", menuService.getAvailableItems());
            model.addAttribute("activeCategory", "all");
        }
        return "customer/menu";
    }

    @GetMapping("/cart")
    public String showCart() {
        return "customer/cart";
    }

    @GetMapping("/orders")
    public String showOrders(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            List<Order> orders = orderRepository.findAll().stream()
                    .filter(o -> "Online".equals(o.getSource()) && email.equals(o.getCustomerId()))
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .toList();
            model.addAttribute("orders", orders);
        }
        return "customer/orders";
    }
}
