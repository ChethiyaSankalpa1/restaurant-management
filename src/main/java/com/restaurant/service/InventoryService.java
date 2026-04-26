package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryAlertRepository alertRepository;
    private final MenuItemRepository menuItemRepository;

    public void deductForOrder(Order order) {
        if (order.getItems() == null) return;
        for (OrderItem item : order.getItems()) {
            menuItemRepository.findById(item.getMenuItemId()).ifPresent(menuItem -> {
                if (menuItem.getInventoryProductId() != null) {
                    inventoryRepository.findById(menuItem.getInventoryProductId()).ifPresent(inv -> {
                        double newQty = (inv.getQuantity() != null ? inv.getQuantity() : 0) - item.getQuantity();
                        inv.setQuantity(Math.max(newQty, 0));
                        inv.setLastUpdated(LocalDateTime.now());
                        inventoryRepository.save(inv);
                        checkAndCreateAlert(inv);
                    });
                }
            });
        }
    }

    public void checkAndCreateAlert(Inventory inv) {
        if (inv.getQuantity() != null && inv.getMinStock() != null
            && inv.getQuantity() <= inv.getMinStock()) {
            // Don't create duplicate active alerts
            boolean hasAlert = alertRepository.findByProductId(inv.getId()).stream()
                .anyMatch(a -> "active".equals(a.getStatus()));
            if (!hasAlert) {
                InventoryAlert alert = InventoryAlert.builder()
                    .productId(inv.getId())
                    .productName(inv.getProductName())
                    .currentStock(inv.getQuantity())
                    .minStock(inv.getMinStock())
                    .alertDate(LocalDateTime.now())
                    .status("active")
                    .branch(inv.getBranch())
                    .build();
                alertRepository.save(alert);
                log.warn("⚠ Low stock alert created for: {}", inv.getProductName());
            }
        }
    }

    public void addStock(String productId, Double quantity, String notes) {
        inventoryRepository.findById(productId).ifPresent(inv -> {
            inv.setQuantity((inv.getQuantity() != null ? inv.getQuantity() : 0) + quantity);
            inv.setLastUpdated(LocalDateTime.now());
            inventoryRepository.save(inv);
            // Resolve alerts if stock is back above min
            if (inv.getMinStock() != null && inv.getQuantity() > inv.getMinStock()) {
                alertRepository.findByProductId(productId).stream()
                    .filter(a -> "active".equals(a.getStatus()))
                    .forEach(a -> {
                        a.setStatus("resolved");
                        alertRepository.save(a);
                    });
            }
        });
    }

    public List<Inventory> getAllInventory() { return inventoryRepository.findAll(); }
    public Optional<Inventory> findById(String id) { return inventoryRepository.findById(id); }
    public Inventory save(Inventory inv) { return inventoryRepository.save(inv); }
    public void delete(String id) { inventoryRepository.deleteById(id); }

    public String getStockStatus(Inventory inv) {
        if (inv.getQuantity() == null || inv.getMinStock() == null) return "Medium";
        if (inv.getQuantity() <= inv.getMinStock()) return "Low";
        if (inv.getMaxStock() != null && inv.getQuantity() >= inv.getMaxStock() * 0.8) return "High";
        return "Medium";
    }

    public double getTotalInventoryValue() {
        return inventoryRepository.findAll().stream()
            .mapToDouble(i -> (i.getQuantity() != null ? i.getQuantity() : 0)
                * (i.getCostPrice() != null ? i.getCostPrice() : 0))
            .sum();
    }
}
