package com.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @GetMapping
    public String settings() { return "redirect:/settings/content"; }

    @GetMapping("/content")
    public String content(Model model) {
        model.addAttribute("pageTitle", "Content Settings");
        model.addAttribute("activeMenu", "settings");
        return "settings/content";
    }

    @GetMapping("/permissions")
    public String permissions(Model model) {
        model.addAttribute("pageTitle", "Permissions");
        model.addAttribute("activeMenu", "settings");
        return "settings/permissions";
    }

    @GetMapping("/modifiers")
    public String modifiers(Model model) {
        model.addAttribute("pageTitle", "Modifiers");
        model.addAttribute("activeMenu", "settings");
        return "settings/modifiers";
    }
}
