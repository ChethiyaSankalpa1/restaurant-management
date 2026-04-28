package com.restaurant.controller;

import com.restaurant.model.User;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SettingService settingService;

    @GetMapping
    public String usersPage(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("activeUsers", users.stream().filter(u -> "active".equals(u.getStatus())).count());
        model.addAttribute("adminCount", users.stream().filter(u -> "ADMIN".equals(u.getRole())).count());
        model.addAttribute("managerCount", users.stream().filter(u -> "MANAGER".equals(u.getRole())).count());
        model.addAttribute("staffCount", users.stream().filter(u -> "STAFF".equals(u.getRole())).count());
        model.addAttribute("newUser", new User());
        model.addAttribute("pageTitle", "User Management");
        model.addAttribute("activeMenu", "users");
        return "users/index";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, RedirectAttributes ra) {
        boolean isNew = (user.getId() == null || user.getId().isEmpty());
        if (isNew) {
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                ra.addFlashAttribute("error", "Password is required for new users.");
                return "redirect:/admin/users";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
        } else {
            // For existing users, only update password if a new one was provided
            userRepository.findById(user.getId()).ifPresent(existing -> {
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    user.setPassword(existing.getPassword());
                } else {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                user.setCreatedAt(existing.getCreatedAt());
            });
        }
        if (user.getStatus() == null || user.getStatus().isEmpty()) user.setStatus("active");
        userRepository.save(user);
        ra.addFlashAttribute("success", isNew ? "User created successfully!" : "User updated successfully!");
        return "redirect:/admin/users";
    }

    @PostMapping("/toggle/{id}")
    public String toggleStatus(@PathVariable String id, RedirectAttributes ra) {
        userRepository.findById(id).ifPresent(u -> {
            u.setStatus("active".equals(u.getStatus()) ? "inactive" : "active");
            userRepository.save(u);
        });
        ra.addFlashAttribute("success", "User status updated.");
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id, RedirectAttributes ra) {
        userRepository.deleteById(id);
        ra.addFlashAttribute("success", "User deleted.");
        return "redirect:/admin/users";
    }
}
