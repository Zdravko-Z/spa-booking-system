package com.fundamentals.spa.controller;

import com.fundamentals.spa.dto.AllBookings;
import com.fundamentals.spa.service.SpaBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final SpaBookingService spaBookingService;

    @GetMapping("/dashboard")
    public String allBookings(Model model){
        List<AllBookings> bookingList = spaBookingService.getAll();
        model.addAttribute("bookings", bookingList);
        return "admin/dashboard";
    }
}
