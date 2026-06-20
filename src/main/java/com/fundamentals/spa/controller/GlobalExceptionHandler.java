package com.fundamentals.spa.controller;

import com.fundamentals.spa.exception.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public String handleEmailExists(EmailAlreadyUsedException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";  // templates/error/not-found.html
    }

    @ExceptionHandler(InvalidBookingStatusException.class)
    public String handleInvalidStatus(InvalidBookingStatusException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(SpaBookingNotFoundException.class)
    public String handleBookingNotFound(SpaBookingNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(SpaRoomNotFoundException.class)
    public String handleRoomNotFound(SpaRoomNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(SpaStaffNotFoundException.class)
    public String handleStaffNotFound(SpaStaffNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(SpaTreatmentNotFoundException.class)
    public String handleTreatmentNotFound(SpaTreatmentNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(SpaUserNotFound.class)
    public String handleUserNotFound(SpaUserNotFound ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public String handleUnauthorizedAction(UnauthorizedActionException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(UsernameOrPasswordMismatch.class)
    public String handleCredentialsMismatch(UsernameOrPasswordMismatch ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(SpaException.class)
    public String handleGeneral(SpaException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
