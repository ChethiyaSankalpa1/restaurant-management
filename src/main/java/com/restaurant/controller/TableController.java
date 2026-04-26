package com.restaurant.controller;

import com.restaurant.model.*;
import com.restaurant.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;

    @GetMapping
    public String tablesPage(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("reservations", tableService.getAllReservations());
        model.addAttribute("availableCount", tableService.countByStatus("Available"));
        model.addAttribute("occupiedCount", tableService.countByStatus("Occupied"));
        model.addAttribute("reservedCount", tableService.countByStatus("Reserved"));
        model.addAttribute("totalCount", tableService.getAllTables().size());
        model.addAttribute("newTable", new RestaurantTable());
        model.addAttribute("newReservation", new Reservation());
        model.addAttribute("pageTitle", "Tables Management");
        model.addAttribute("activeMenu", "tables");
        return "tables/index";
    }

    @PostMapping("/save")
    public String saveTable(@ModelAttribute RestaurantTable table, RedirectAttributes ra) {
        tableService.save(table);
        ra.addFlashAttribute("success", "Table saved successfully!");
        return "redirect:/tables";
    }

    @PostMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changeStatus(@PathVariable String id, @RequestParam String status) {
        try {
            RestaurantTable t = tableService.changeStatus(id, status);
            return ResponseEntity.ok(Map.of("success", true, "status", t.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteTable(@PathVariable String id, RedirectAttributes ra) {
        tableService.delete(id);
        ra.addFlashAttribute("success", "Table deleted");
        return "redirect:/tables";
    }

    @PostMapping("/reservation/save")
    public String saveReservation(@ModelAttribute Reservation reservation, RedirectAttributes ra) {
        tableService.saveReservation(reservation);
        ra.addFlashAttribute("success", "Reservation saved!");
        return "redirect:/tables";
    }
}
