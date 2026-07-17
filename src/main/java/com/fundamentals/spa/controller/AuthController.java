package com.fundamentals.spa.controller;

import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.SecurityUser;
import com.fundamentals.spa.entity.enums.UserRole;
import com.fundamentals.spa.exception.SpaException;
import com.fundamentals.spa.service.GuestService;
import com.fundamentals.spa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final GuestService guestService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(@AuthenticationPrincipal SecurityUser user, Model model) {
        if (user != null) {
            UserRole role = user.getRole();
            if (role == UserRole.ADMIN) {
                return "redirect:/admin/dashboard";
            }else {
                return "redirect:/bookings/my-bookings";
            }
        }
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDto") RegisterDto registerDto,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            UUID userId = userService.register(registerDto);
            guestService.createGuest(registerDto, userId);
            return "redirect:/login";
        } catch (SpaException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
