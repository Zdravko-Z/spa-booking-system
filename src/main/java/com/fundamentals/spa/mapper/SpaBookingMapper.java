package com.fundamentals.spa.mapper;

import com.fundamentals.spa.dto.*;
import com.fundamentals.spa.entity.SpaBooking;

public class SpaBookingMapper {
    public static SpaBookingDto toDto(SpaBooking booking){
        return SpaBookingDto.builder()
                .id(booking.getId())
                .bookingDate(booking.getBookingDate())
                .confirmationCode(booking.getConfirmationCode())
                .durationMinutes(booking.getDurationMinutes())
                .endTime(booking.getEndTime())
                .guestDto(GuestMapper.toDto(booking.getGuest()))
                .notes(booking.getNotes())
                .startTime(booking.getStartTime())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .spaRoomDto(SpaRoomMapper.toDto(booking.getSpaRoom()))
                .spaStaffDto(booking.getStaff() != null ? SpaStaffMapper.toDto(booking.getStaff()) : null)
                .build();
    }
}
