package com.fundamentals.spa.dto;

import com.fundamentals.spa.entity.Guest;
import com.fundamentals.spa.entity.SpaRoom;
import com.fundamentals.spa.entity.User;
import com.fundamentals.spa.entity.enums.BookingStatus;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class SpaBookingDto {
    private String confirmationCode;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int durationMinutes;
    private BigDecimal totalPrice;
    private BookingStatus status = BookingStatus.PENDING;
    private String notes;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
    private Guest guest;
    private SpaRoom spaRoom;
    private User staff;
    private List<UUID> treatments;
}
