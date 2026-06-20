package com.fundamentals.spa.controller;

import com.fundamentals.spa.dto.SpaBookingDto;
import com.fundamentals.spa.entity.enums.UserRole;
import com.fundamentals.spa.service.SpaBookingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final SpaBookingService spaBookingService;

    @GetMapping
    public String allBookings(Model model){
        List<SpaBookingDto> bookingList = spaBookingService.getAll();
        model.addAttribute("bookingsDto", bookingList);
        return "admin";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable UUID id,
                         RedirectAttributes redirectAttributes,
                         HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        UserRole role = (UserRole) session.getAttribute("user_role");

        spaBookingService.cancel(id, userId, role);
        redirectAttributes.addFlashAttribute("success", "Booking canceled");
        return "redirect:/admin";
    }
}
