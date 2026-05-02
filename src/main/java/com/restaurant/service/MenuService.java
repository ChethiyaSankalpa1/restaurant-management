package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public List<MenuItem> getAllMenuItems() { 
        var items = menuItemRepository.findAll();
        log.info("Fetched {} total menu items", items.size());
        return items; 
    }
    @Cacheable("menuItems")
    public List<MenuItem> getAvailableItems() { 
        var all = menuItemRepository.findAll();
        var available = all.stream()
            .filter(i -> i != null && (i.getAvailable() == null || i.getAvailable()))
            .toList(); 
        log.info("Fetched {} available menu items out of {} total", available.size(), all.size());
        return available;
    }
    public List<MenuItem> getByCategory(String categoryId) { return menuItemRepository.findByCategoryId(categoryId); }
    public Optional<MenuItem> findById(String id) { return menuItemRepository.findById(id); }
    @CacheEvict(value = "menuItems", allEntries = true)
    public MenuItem save(MenuItem item) {
        if (item.getCategoryId() != null) {
            categoryRepository.findById(item.getCategoryId()).ifPresent(c -> item.setCategoryName(c.getName()));
        }
        return menuItemRepository.save(item);
    }
    @CacheEvict(value = "menuItems", allEntries = true)
    public void delete(String id) { menuItemRepository.deleteById(id); }
    public List<Category> getAllCategories() { return categoryRepository.findAll(); }
    public Optional<Category> findCategoryById(String id) { return categoryRepository.findById(id); }
    public Category saveCategory(Category cat) { return categoryRepository.save(cat); }
    public void deleteCategory(String id) { categoryRepository.deleteById(id); }
    public List<MenuItem> searchMenuItems(String query) { return menuItemRepository.findByNameContainingIgnoreCase(query); }
}
