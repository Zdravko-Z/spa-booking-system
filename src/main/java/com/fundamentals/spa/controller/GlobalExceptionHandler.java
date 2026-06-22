package com.fundamentals.spa.controller;

import com.fundamentals.spa.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public String handleEmailExists(EmailAlreadyUsedException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidBookingStatusException.class)
    public String handleInvalidStatus(InvalidBookingStatusException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SpaBookingNotFoundException.class)
    public String handleBookingNotFound(SpaBookingNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SpaRoomNotFoundException.class)
    public String handleRoomNotFound(SpaRoomNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SpaStaffNotFoundException.class)
    public String handleStaffNotFound(SpaStaffNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SpaTreatmentNotFoundException.class)
    public String handleTreatmentNotFound(SpaTreatmentNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SpaUserNotFound.class)
    public String handleUserNotFound(SpaUserNotFound ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedActionException.class)
    public String handleUnauthorizedAction(UnauthorizedActionException ex, Model model) {
        model.addAttribute("error", ex.getMessage());

        return "error";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UsernameOrPasswordMismatch.class)
    public String handleCredentialsMismatch(UsernameOrPasswordMismatch ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpaException.class)
    public String handleGeneral(SpaException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String catchAll(Exception ex, Model model){
        model.addAttribute("error", ex.getMessage());
        log.error("error", ex);
        return "error";
    }
}
