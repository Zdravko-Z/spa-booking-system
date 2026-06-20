package com.fundamentals.spa.controller;

import com.fundamentals.spa.dto.SpaStaffDto;
import com.fundamentals.spa.service.SpaStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;

@Controller
@RequestMapping("/admin/staff")
@RequiredArgsConstructor
public class AdminStaffController {
    private final SpaStaffService spaStaffService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("staffList", spaStaffService.getAll());
        return "admin/staff/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("staffDto", spaStaffService.getById(id));
        return "admin/staff/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("staffDto") SpaStaffDto staffDto,
                         BindingResult result,
                         @PathVariable UUID id,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/staff/form";
        }
        spaStaffService.update(id, staffDto);
        redirectAttributes.addFlashAttribute("success", "Staff member updated successfully");
        return "redirect:/admin/staff";
    }
}