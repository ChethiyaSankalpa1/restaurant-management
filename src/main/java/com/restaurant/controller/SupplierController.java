package com.restaurant.controller;

import com.restaurant.model.*;
import com.restaurant.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public String suppliersPage(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("payments", supplierService.getAllPayments());
        model.addAttribute("newSupplier", new Supplier());
        model.addAttribute("pageTitle", "Suppliers Management");
        model.addAttribute("activeMenu", "suppliers");
        return "suppliers/index";
    }

    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute Supplier supplier, RedirectAttributes ra) {
        if (supplier.getStatus() == null) supplier.setStatus("active");
        if (supplier.getProducts() == null) supplier.setProducts(new ArrayList<>());
        supplierService.save(supplier);
        ra.addFlashAttribute("success", "Supplier saved!");
        return "redirect:/suppliers";
    }

    @PostMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable String id, RedirectAttributes ra) {
        supplierService.delete(id);
        ra.addFlashAttribute("success", "Supplier deleted");
        return "redirect:/suppliers";
    }

    @PostMapping("/payment/save")
    public String savePayment(@ModelAttribute SupplierPayment payment, RedirectAttributes ra) {
        if (payment.getDate() == null) payment.setDate(LocalDate.now());
        if (payment.getStatus() == null) payment.setStatus("Paid");
        supplierService.savePayment(payment);
        ra.addFlashAttribute("success", "Payment recorded!");
        return "redirect:/suppliers";
    }

    @GetMapping("/{id}/payments")
    public String supplierPayments(@PathVariable String id, Model model) {
        supplierService.findById(id).ifPresent(s -> model.addAttribute("supplier", s));
        model.addAttribute("payments", supplierService.getPayments(id));
        model.addAttribute("pageTitle", "Supplier Payments");
        model.addAttribute("activeMenu", "suppliers");
        return "suppliers/payments";
    }
}
