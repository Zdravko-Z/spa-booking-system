package com.fundamentals.spa.dto;

import com.fundamentals.spa.entity.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaBookingDto {
    private UUID id;

    private String confirmationCode;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private int durationMinutes;

    private BigDecimal totalPrice;

    private BookingStatus status = BookingStatus.PENDING;

    private String notes;

    private GuestDto guestDto;

    private SpaRoomDto spaRoomDto;

    @NotNull
    private SpaStaffDto spaStaffDto;
}
