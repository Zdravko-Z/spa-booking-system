package com.fundamentals.spa.dto;

import com.fundamentals.spa.entity.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AllBookings(
        UUID id,
        String confirmationCode,
        LocalDate bookingDate,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal totalPrice,
        BookingStatus status,
        String notes,
        int durationMinutes,
        String guestFirstName,
        String guestLastName,
        String roomName,
        String staffFirstName,
        String staffLastName
) {}

