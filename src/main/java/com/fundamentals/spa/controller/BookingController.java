package com.fundamentals.spa.controller;

import com.fundamentals.spa.dto.SpaBookingDto;
import com.fundamentals.spa.dto.SpaBookingForm;
import com.fundamentals.spa.entity.enums.UserRole;
import com.fundamentals.spa.exception.SpaException;
import com.fundamentals.spa.service.SpaBookingService;
import com.fundamentals.spa.service.SpaStaffService;
import com.fundamentals.spa.service.SpaTreatmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final SpaBookingService spaBookingService;
    private final SpaTreatmentService spaTreatmentService;
    private final SpaStaffService spaStaffService;

    @GetMapping("/my-bookings")
    public String myBookings(HttpSession session, Model model){
        UUID userId = (UUID) session.getAttribute("user_id");
        try {
            List<SpaBookingDto> bookingList = spaBookingService.getAllForGuest(userId);
            model.addAttribute("bookingList", bookingList);
        }catch (SpaException e){
            model.addAttribute("error", "Guest not found");
        }
        return "bookings/my-bookings";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable UUID id,
                         RedirectAttributes redirectAttributes,
                         HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        UserRole role = (UserRole) session.getAttribute("user_role");

        spaBookingService.cancel(id, userId, role);
        redirectAttributes.addFlashAttribute("success", "Booking cancelled");
        if (role == UserRole.ADMIN){
            return "redirect:/admin/dashboard";
        }else {
            return "redirect:/bookings/my-bookings";
        }
    }

    @GetMapping("/create")
    public String datePicker(Model model){
        model.addAttribute("today", LocalDate.now());
        return "bookings/step1";
    }

    @PostMapping("/create/select-slots")
    public String selectSlots(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
                              LocalDate bookingDate, Model model){
        if (bookingDate.isBefore(LocalDate.now())){
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("error", "Please select a valid date");
            return "bookings/step1";
        }

        List<String> slots = spaBookingService.getAvailableSlotsForDate(bookingDate);
        if (slots.isEmpty()){
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("error", "No available slots for this date. Please pick a different date");
            return "bookings/step1";
        }

        model.addAttribute("bookingDate", bookingDate);
        model.addAttribute("availableSlots", slots);
        model.addAttribute("treatments", spaTreatmentService.getAllNotDeleted());
        model.addAttribute("staffList", spaStaffService.getAll());
        model.addAttribute("bookingForm", new SpaBookingForm());
        return "bookings/step2";
    }

    @PostMapping("/create")
    public String createBooking(@Valid @ModelAttribute("bookingForm") SpaBookingForm bookingForm,
                                BindingResult result, HttpSession session, Model model,
                                RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            model.addAttribute("bookingDate", bookingForm.getBookingDate());
            model.addAttribute("availableSlots", spaBookingService.getAvailableSlotsForDate(bookingForm.getBookingDate()));
            model.addAttribute("treatments", spaTreatmentService.getAllNotDeleted());
            model.addAttribute("staffList", spaStaffService.getAll());
            return "bookings/step2";
        }

        UUID userId = (UUID) session.getAttribute("user_id");

        try {
            spaBookingService.create(bookingForm, userId);
            redirectAttributes.addFlashAttribute("success", "Booking confirmed");
            return "redirect:/bookings/my-bookings";
        }catch (SpaException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookingDate", bookingForm.getBookingDate());
            model.addAttribute("availableSlots", spaBookingService.getAvailableSlotsForDate(bookingForm.getBookingDate()));
            model.addAttribute("treatments", spaTreatmentService.getAllNotDeleted());
            model.addAttribute("staffList", spaStaffService.getAll());
            return "bookings/step2";
        }
    }
}