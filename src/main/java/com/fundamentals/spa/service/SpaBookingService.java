package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.SpaBookingDto;
import com.fundamentals.spa.dto.SpaBookingForm;
import com.fundamentals.spa.entity.*;
import com.fundamentals.spa.entity.enums.BookingStatus;
import com.fundamentals.spa.entity.enums.UserRole;
import com.fundamentals.spa.exception.*;
import com.fundamentals.spa.mapper.SpaBookingMapper;
import com.fundamentals.spa.repository.SpaBookingRepository;
import com.fundamentals.spa.repository.SpaBookingTreatmentRepository;
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
    private final SpaTreatmentService spaTreatmentService;
    private final SpaRoomService spaRoomService;
    private final SpaBookingTreatmentRepository spaBookingTreatmentRepository;
    private final SpaStaffService spaStaffService;

    @Transactional(readOnly = true)
    public List<SpaBookingDto> getAll(){
        return spaBookingRepository.findAll().stream().map(SpaBookingMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SpaBookingDto getById(UUID id){
        return SpaBookingMapper.toDto(spaBookingRepository.findById(id).orElseThrow(() -> new SpaBookingNotFoundException("Booking not found")));
    }

    @Transactional(readOnly = true)
    public List<SpaBookingDto> getAllForGuest(UUID userId){
        Guest guest = guestService.getByUser(userId);
        if (guest == null){
            return new ArrayList<>();
        }
        return spaBookingRepository.getAllByGuest(guest).stream().map(SpaBookingMapper::toDto).toList();
    }

    public void cancel(UUID bookingId, UUID userId, UserRole role){
        SpaBooking booking = spaBookingRepository.findById(bookingId)
                .orElseThrow(() -> new SpaBookingNotFoundException("Booking not found"));
        if (role == UserRole.CLIENT){
            Guest guest = guestService.getByUser(userId);
            if (!booking.getGuest().getId().equals(guest.getId())){
                throw new UnauthorizedActionException("You cannot cancel this booking");
            }
        }

        if (booking.getStatus() == BookingStatus.CANCELLED){
            throw new InvalidBookingStatusException("Booking is already cancelled");
        }else if(booking.getBookingDate().isBefore(LocalDate.now())){
            throw new SpaException("Booking already complete");
        }

        booking.setStatus(BookingStatus.CANCELLED);
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
        LocalTime closing = LocalTime.of(20, 0);
        while (time.isBefore(closing)) {
            slots.add(time.toString());
            time = time.plusMinutes(30);
        }
        return slots;
    }

    public void create(SpaBookingForm dto, UUID user){
        if (dto.getTreatmentIds() == null || dto.getTreatmentIds().isEmpty()){
            throw new InvalidBookingStatusException("Please select at least one treatment");
        }

        Guest guest = guestService.getByUser(user);

        List<SpaTreatment> treatments = dto.getTreatmentIds().stream()
                .map(spaTreatmentService::getEntityById)
                .toList();

        int totalMinutes = treatments.stream().mapToInt(SpaTreatment::getDurationMinutes)
                .sum();

        LocalTime endTime = dto.getStartTime().plusMinutes(totalMinutes);

        SpaRoom room = spaRoomService
                .getAvailableRoom(dto.getBookingDate(), dto.getStartTime(), endTime)
                .stream().findFirst().orElseThrow(() -> new SpaRoomNotFoundException("No available rooms"));

        SpaStaff staff = null;
        if (dto.getStaffId() != null) {
            staff = spaStaffService.getEntityById(dto.getStaffId());
            List<SpaStaff> available = spaStaffService.getAvailableEntities(dto.getBookingDate(), dto.getStartTime(), endTime);
            if (!available.contains(staff)) {
                throw new SpaStaffNotFoundException("Selected therapist is not available for this time");
            }
        } else {
            List<SpaStaff> available = spaStaffService.getAvailableEntities(dto.getBookingDate(), dto.getStartTime(), endTime);
            if (!available.isEmpty()) {
                staff = available.get(0);
            }
        }

        SpaBooking booking = new SpaBooking();
        booking.setConfirmationCode(generateConfirmationCode());
        booking.setGuest(guest);
        booking.setSpaRoom(room);
        booking.setStaff(staff);
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