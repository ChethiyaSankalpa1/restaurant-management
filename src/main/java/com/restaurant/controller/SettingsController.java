package com.restaurant.controller;

import com.restaurant.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final SettingService settingService;

    @GetMapping
    public String settings() { return "redirect:/settings/content"; }

    @GetMapping("/content")
    public String content(Model model) {
        model.addAttribute("pageTitle", "Content Settings");
        model.addAttribute("activeMenu", "settings");
        model.addAttribute("currencySymbol", settingService.getSetting("currency_symbol", "Rs."));
        model.addAttribute("currencyName", settingService.getSetting("currency_name", "LKR"));
        return "settings/content";
    }

    @PostMapping("/save")
    public String saveSettings(@RequestParam String currencySymbol, @RequestParam String currencyName, RedirectAttributes ra) {
        settingService.saveSetting("currency_symbol", currencySymbol);
        settingService.saveSetting("currency_name", currencyName);
        ra.addFlashAttribute("success", "Settings updated successfully!");
        return "redirect:/settings/content";
    }

    @GetMapping("/permissions")
    public String permissions(Model model) {
        model.addAttribute("pageTitle", "Permissions");
        model.addAttribute("activeMenu", "settings");

        // Load all saved permission settings as a simple JSON string for JS
        Map<String, String> allSettings = settingService.getAllSettings();
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> e : allSettings.entrySet()) {
            if (e.getKey().startsWith("perm_")) {
                if (!first) json.append(",");
                json.append("\"").append(e.getKey()).append("\":\"").append(e.getValue()).append("\"");
                first = false;
            }
        }
        json.append("}");
        model.addAttribute("permissionsJson", json.toString());

        return "settings/permissions";
    }

    @PostMapping("/permissions/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> savePermissions(@RequestBody Map<String, String> perms) {
        try {
            for (Map.Entry<String, String> entry : perms.entrySet()) {
                settingService.saveSetting(entry.getKey(), entry.getValue());
            }
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/modifiers")
    public String modifiers(Model model) {
        model.addAttribute("pageTitle", "Modifiers");
        model.addAttribute("activeMenu", "settings");
        return "settings/modifiers";
    }
}
