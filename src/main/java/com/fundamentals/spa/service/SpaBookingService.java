package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.SpaBookingDto;
import com.fundamentals.spa.dto.SpaRoomDto;
import com.fundamentals.spa.entity.*;
import com.fundamentals.spa.entity.enums.BookingStatus;
import com.fundamentals.spa.entity.enums.UserRole;
import com.fundamentals.spa.exception.InvalidBookingStatusException;
import com.fundamentals.spa.exception.SpaBookingNotFoundException;
import com.fundamentals.spa.exception.SpaRoomNotFoundException;
import com.fundamentals.spa.exception.UnauthorizedActionException;
import com.fundamentals.spa.mapper.SpaBookingMapper;
import com.fundamentals.spa.mapper.SpaTreatmentMapper;
import com.fundamentals.spa.repository.SpaBookingRepository;
import com.fundamentals.spa.repository.SpaBookingTreatmentRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SpaBookingService {
    private final SpaBookingRepository spaBookingRepository;
    private final GuestService guestService;
    private final UserService userService;
    private final SpaTreatmentService spaTreatmentService;
    private final SpaRoomService spaRoomService;
    private final SpaBookingTreatmentRepository spaBookingTreatmentRepository;

    @Transactional(readOnly = true)
    public List<SpaBookingDto> getAll(){
        return spaBookingRepository.findAll().stream().map(SpaBookingMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SpaBookingDto getById(UUID id){
       return SpaBookingMapper.toDto(spaBookingRepository.findById(id).orElseThrow(() -> new SpaRoomNotFoundException("Booking not found")));
    }

    public List<SpaBookingDto> getAllForGuest(HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        Guest guest = guestService.getByUser(userId);
        return spaBookingRepository.getAllByGuest(guest.getId()).stream().map(SpaBookingMapper::toDto).toList();
    }

    public void cancel(UUID bookingId, UUID userId){
        Guest guest = guestService.getByUser(userId);
        SpaBooking booking = spaBookingRepository.findById(bookingId)
                .orElseThrow(() -> new SpaBookingNotFoundException("Booking not found"));

        if (!booking.getGuest().getId().equals(guest.getId()) &&
        userService.getById(userId).getRole() != UserRole.ADMIN){
            throw new UnauthorizedActionException("You cannot cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED){
            throw new InvalidBookingStatusException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
    }

    public void confirm(UUID bookingId){
        SpaBooking booking = spaBookingRepository.findById(bookingId)
                .orElseThrow(() -> new SpaBookingNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING){
            throw new InvalidBookingStatusException("This booking cannot be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableSlotsForDate(LocalDate date) {
        List<String> allSlots = generateSlots();
        List<String> takenSlots = spaBookingRepository.findTakenSlots(date).stream().map(LocalTime::toString).toList();
        allSlots.removeAll(takenSlots);
        return allSlots;
    }

    private List<String> generateSlots() {
        List<String> slots = new ArrayList<>();
        LocalTime time = LocalTime.of(9, 0);
        LocalTime closing = LocalTime.of(18, 0);
        while (time.isBefore(closing)) {
            slots.add(time.toString());
            time = time.plusMinutes(30);
        }
        return slots;
    }


    public void create(SpaBookingDto dto, UUID user){
        Guest guest = guestService.getByUser(user);

        List<SpaTreatment> treatments = dto.getTreatments().stream()
                .map(spaTreatmentService::getById)
                .map(SpaTreatmentMapper::toEntity)
                .toList();

        int totalMinutes = treatments.stream().mapToInt(SpaTreatment::getDurationMinutes)
                .sum();

        LocalTime endTime = dto.getStartTime().plusMinutes(totalMinutes);

        SpaRoom room = spaRoomService
                .getAvailableRoom(dto.getBookingDate(), dto.getStartTime(), endTime)
                .stream().findFirst().orElseThrow(() -> new SpaRoomNotFoundException("No available rooms"));

        SpaBooking booking = new SpaBooking();
        booking.setConfirmationCode(generateConfirmationCode());
        booking.setGuest(guest);
        booking.setSpaRoom(room);
        booking.setBookingDate(dto.getBookingDate());
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(endTime);
        booking.setDurationMinutes(totalMinutes);
        booking.setStatus(BookingStatus.PENDING);
        booking.setNotes(dto.getNotes());
        booking.setTotalPrice(BigDecimal.ZERO);
        spaBookingRepository.save(booking);

        BigDecimal total = BigDecimal.ZERO;
        for (SpaTreatment treatment : treatments) {
            SpaBookingTreatment bt = new SpaBookingTreatment();
            bt.setBooking(booking);
            bt.setTreatment(treatment);
            bt.setPriceAtBooking(treatment.getPrice());
            spaBookingTreatmentRepository.save(bt);
            total = total.add(treatment.getPrice());
        }

        booking.setTotalPrice(total);
    }

    public String generateConfirmationCode(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 10);
    }
}
