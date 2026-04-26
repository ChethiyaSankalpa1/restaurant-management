package com.restaurant.controller;

import com.restaurant.model.Inventory;
import com.restaurant.service.InventoryService;
import com.restaurant.service.SupplierService;
import com.restaurant.repository.InventoryAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final SupplierService supplierService;
    private final InventoryAlertRepository alertRepository;

    @GetMapping
    public String inventoryPage(Model model) {
        var inventory = inventoryService.getAllInventory();
        long lowStock = inventory.stream().filter(i ->
            i.getQuantity() != null && i.getMinStock() != null && i.getQuantity() <= i.getMinStock()).count();

        model.addAttribute("inventory", inventory);
        model.addAttribute("totalProducts", inventory.size());
        model.addAttribute("lowStockCount", lowStock);
        model.addAttribute("activeSuppliers", supplierService.getActiveSuppliers().size());
        model.addAttribute("totalValue", inventoryService.getTotalInventoryValue());
        model.addAttribute("alerts", alertRepository.findByStatus("active"));
        model.addAttribute("newItem", new Inventory());
        model.addAttribute("pageTitle", "Inventory Management");
        model.addAttribute("activeMenu", "inventory");
        return "inventory/index";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Inventory item, RedirectAttributes ra) {
        item.setLastUpdated(LocalDateTime.now());
        inventoryService.save(item);
        ra.addFlashAttribute("success", "Product saved!");
        return "redirect:/inventory";
    }

    @PostMapping("/{id}/add-stock")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addStock(@PathVariable String id, @RequestParam Double quantity) {
        try {
            inventoryService.addStock(id, quantity, "Manual addition");
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes ra) {
        inventoryService.delete(id);
        ra.addFlashAttribute("success", "Product deleted");
        return "redirect:/inventory";
    }
}
