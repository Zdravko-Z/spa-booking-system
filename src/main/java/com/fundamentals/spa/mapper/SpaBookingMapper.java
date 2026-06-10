package com.fundamentals.spa.mapper;

import com.fundamentals.spa.dto.SpaBookingDto;
import com.fundamentals.spa.dto.SpaStaffDto;
import com.fundamentals.spa.entity.SpaBooking;
import com.fundamentals.spa.entity.SpaStaff;

public class SpaBookingMapper {
    public static SpaBookingDto toDto(SpaBooking booking){
        return SpaBookingDto.builder()
                .bookingDate(booking.getBookingDate())
                .confirmationCode(booking.getConfirmationCode())
                .durationMinutes(booking.getDurationMinutes())
                .endTime(booking.getEndTime())
                .guest(booking.getGuest())
                .notes(booking.getNotes())
                .spaRoom(booking.getSpaRoom())
                .staff(booking.getStaff())
                .startTime(booking.getStartTime())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .build();
    }

    public static SpaBooking toEntity(SpaBookingDto dto){
        SpaBooking booking = new SpaBooking();
        booking.setStatus(dto.getStatus());
        booking.setBookingDate(dto.getBookingDate());
        booking.setDurationMinutes(dto.getDurationMinutes());
        booking.setGuest(dto.getGuest());
        booking.setConfirmationCode(dto.getConfirmationCode());
        booking.setNotes(dto.getNotes());
        booking.setSpaRoom(dto.getSpaRoom());
        booking.setStaff(dto.getStaff());
        booking.setStartTime(dto.getStartTime());
        booking.setTotalPrice(dto.getTotalPrice());
        booking.setEndTime(dto.getEndTime());

        return booking;
    }
}
