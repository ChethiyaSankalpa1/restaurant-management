package com.restaurant.controller;

import com.restaurant.model.Branch;
import com.restaurant.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/branches")
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public String branchesPage(Model model) {
        model.addAttribute("branches", branchService.getAllBranches());
        model.addAttribute("newBranch", new Branch());
        model.addAttribute("pageTitle", "Branch Management");
        model.addAttribute("activeMenu", "branches");
        return "branches/index";
    }

    @PostMapping("/save")
    public String saveBranch(@ModelAttribute Branch branch, RedirectAttributes ra) {
        if (branch.getStatus() == null) branch.setStatus("active");
        branchService.save(branch);
        ra.addFlashAttribute("success", "Branch saved!");
        return "redirect:/branches";
    }

    @PostMapping("/delete/{id}")
    public String deleteBranch(@PathVariable String id, RedirectAttributes ra) {
        branchService.delete(id);
        ra.addFlashAttribute("success", "Branch deleted");
        return "redirect:/branches";
    }
}
