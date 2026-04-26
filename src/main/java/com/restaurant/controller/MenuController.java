package com.restaurant.controller;

import com.restaurant.model.*;
import com.restaurant.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public String menuPage(@RequestParam(required = false) String search,
                           @RequestParam(required = false) String categoryId, Model model) {
        var items = (search != null && !search.isEmpty()) ? menuService.searchMenuItems(search) :
                    (categoryId != null && !categoryId.isEmpty()) ? menuService.getByCategory(categoryId) :
                    menuService.getAllMenuItems();
        model.addAttribute("menuItems", items);
        model.addAttribute("categories", menuService.getAllCategories());
        model.addAttribute("newItem", new MenuItem());
        model.addAttribute("newCategory", new Category());
        model.addAttribute("search", search);
        model.addAttribute("filterCategory", categoryId);
        model.addAttribute("pageTitle", "Menu Management");
        model.addAttribute("activeMenu", "menu");
        return "menu/index";
    }

    @PostMapping("/item/save")
    public String saveItem(@ModelAttribute MenuItem item, RedirectAttributes ra) {
        if (item.getAvailable() == null) item.setAvailable(true);
        menuService.save(item);
        ra.addFlashAttribute("success", "Menu item saved!");
        return "redirect:/menu";
    }

    @PostMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable String id, RedirectAttributes ra) {
        menuService.delete(id);
        ra.addFlashAttribute("success", "Item deleted");
        return "redirect:/menu";
    }

    @PostMapping("/category/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes ra) {
        if (category.getActive() == null) category.setActive(true);
        menuService.saveCategory(category);
        ra.addFlashAttribute("success", "Category saved!");
        return "redirect:/menu";
    }

    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable String id, RedirectAttributes ra) {
        menuService.deleteCategory(id);
        ra.addFlashAttribute("success", "Category deleted");
        return "redirect:/menu";
    }

    @GetMapping("/public")
    public String publicMenu(@RequestParam(required = false) String table, Model model) {
        model.addAttribute("categories", menuService.getAllCategories());
        model.addAttribute("menuItems", menuService.getAvailableItems());
        model.addAttribute("tableNumber", table);
        return "menu/public";
    }
}
