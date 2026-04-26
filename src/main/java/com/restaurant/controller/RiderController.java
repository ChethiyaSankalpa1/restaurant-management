package com.restaurant.controller;

import com.restaurant.model.Rider;
import com.restaurant.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/riders")
public class RiderController {

    private final RiderService riderService;

    @GetMapping
    public String ridersPage(Model model) {
        model.addAttribute("riders", riderService.getAllRiders());
        model.addAttribute("newRider", new Rider());
        model.addAttribute("pageTitle", "Riders Management");
        model.addAttribute("activeMenu", "riders");
        return "riders/index";
    }

    @PostMapping("/save")
    public String saveRider(@ModelAttribute Rider rider, RedirectAttributes ra) {
        if (rider.getStatus() == null) rider.setStatus("active");
        riderService.save(rider);
        ra.addFlashAttribute("success", "Rider saved!");
        return "redirect:/riders";
    }

    @PostMapping("/delete/{id}")
    public String deleteRider(@PathVariable String id, RedirectAttributes ra) {
        riderService.delete(id);
        ra.addFlashAttribute("success", "Rider deleted");
        return "redirect:/riders";
    }
}
