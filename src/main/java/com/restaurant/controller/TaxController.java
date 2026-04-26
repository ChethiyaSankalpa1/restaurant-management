package com.restaurant.controller;

import com.restaurant.model.Tax;
import com.restaurant.repository.TaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings/tax")
public class TaxController {

    private final TaxRepository taxRepository;

    public List<Tax> getActiveTaxes() { return taxRepository.findByStatus("active"); }

    @GetMapping
    public String taxPage(Model model) {
        model.addAttribute("taxes", taxRepository.findAll());
        model.addAttribute("pageTitle", "Tax Management");
        model.addAttribute("activeMenu", "tax");
        return "settings/tax";
    }

    @PostMapping("/save")
    public String saveTax(@ModelAttribute Tax tax, RedirectAttributes ra) {
        taxRepository.save(tax);
        ra.addFlashAttribute("success", "Tax saved successfully");
        return "redirect:/settings/tax";
    }

    @PostMapping("/delete/{id}")
    public String deleteTax(@PathVariable String id, RedirectAttributes ra) {
        taxRepository.deleteById(id);
        ra.addFlashAttribute("success", "Tax deleted");
        return "redirect:/settings/tax";
    }
}
