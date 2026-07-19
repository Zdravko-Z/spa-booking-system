package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.AllBookings;
import com.fundamentals.spa.dto.NotificationRequest;
import com.fundamentals.spa.dto.SpaBookingDto;
import com.fundamentals.spa.dto.SpaBookingForm;
import com.fundamentals.spa.entity.*;
import com.fundamentals.spa.entity.enums.BookingStatus;
import com.fundamentals.spa.entity.enums.UserRole;
import com.fundamentals.spa.exception.*;
import com.fundamentals.spa.feign.NotificationService;
import com.fundamentals.spa.mapper.SpaBookingMapper;
import com.fundamentals.spa.repository.SpaBookingRepository;
import com.fundamentals.spa.repository.SpaBookingTreatmentRepository;
import com.fundamentals.spa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public List<AllBookings> getAll(){
        int updated = spaBookingRepository.updateTodayCompletedBookings(
                BookingStatus.PENDING, BookingStatus.COMPLETED,
                LocalDate.now(), LocalTime.now()
        );
        log.info("Updated status of {} bookings", updated);
        return spaBookingRepository.findBookingSummaries();
    }

    @Transactional(readOnly = true)
    public SpaBookingDto getById(UUID id){
        SpaBookingDto dto =  SpaBookingMapper.toDto(spaBookingRepository.findById(id).orElseThrow(() -> new SpaBookingNotFoundException("Booking not found")));
        log.info("found booking {}", dto.getId());
        return dto;
    }

    public List<AllBookings> getAllForGuest(UUID userId){
        Guest guest = guestService.getByUser(userId);
        if (guest == null){
            return new ArrayList<>();
        }
        int updated = spaBookingRepository.updateTodayCompletedBookings(
                BookingStatus.PENDING, BookingStatus.COMPLETED,
                LocalDate.now(), LocalTime.now()
        );
        log.info("updated status of {} bookings", updated);
        return spaBookingRepository.findSummariesByGuest(guest);
    }

    public void cancel(UUID bookingId, UUID userId, UserRole role){
        SpaBooking booking = spaBookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("booking {} was not found", bookingId);
                    return new SpaBookingNotFoundException("Booking not found");
                });
        if (role == UserRole.CLIENT){
            Guest guest = guestService.getByUser(userId);
            if (!booking.getGuest().getId().equals(guest.getId())){
                log.warn("user {} without needed permission is trying to cancel booking {}", userId, bookingId);
                throw new UnauthorizedActionException("You cannot cancel this booking");
            }
        }

        if (booking.getStatus() == BookingStatus.CANCELLED){
            log.info("booking {} is already cancelled", bookingId);
            throw new InvalidBookingStatusException("Booking is already cancelled");
        }else if(booking.getBookingDate().isBefore(LocalDate.now())){
            log.info("booking {} is already complete", bookingId);
            throw new SpaException("Booking already complete");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        log.info("cancelled booking {}", bookingId);
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableSlotsForDate(LocalDate date) {
        List<String> allSlots = generateSlots(date);
        List<String> takenSlots = spaBookingRepository.findTakenSlots(date).stream().map(LocalTime::toString).toList();
        allSlots.removeAll(takenSlots);
        return allSlots;
    }

    private List<LocalTime> workingHours(LocalDate date){
        LocalTime time;
        LocalTime closing;
        if (date.getDayOfWeek().getValue() == 6){
            time = LocalTime.of(10, 0);
            closing = LocalTime.of(18, 0);
        }else {
            time = LocalTime.of(9, 0);
            closing = LocalTime.of(20, 0);
        }
        return List.of(time, closing);
    }

    public void create(SpaBookingForm dto, UUID userId){
        if (dto.getTreatmentIds() == null || dto.getTreatmentIds().isEmpty()){
            throw new InvalidBookingStatusException("Please select at least one treatment");
        }

        Guest guest = guestService.getByUser(userId);

        List<SpaTreatment> treatments = dto.getTreatmentIds().stream()
                .map(spaTreatmentService::getEntityById)
                .toList();

        int totalMinutes = treatments.stream().mapToInt(SpaTreatment::getDurationMinutes)
                .sum();

        LocalTime endTime = dto.getStartTime().plusMinutes(totalMinutes);

        if (endTime.isAfter(workingHours(dto.getBookingDate()).get(1))){
            throw new SpaException("Not enough time for all selected treatments today");
        }

        SpaRoom room = spaRoomService
                .getAvailableRoom(dto.getBookingDate(), dto.getStartTime(), endTime)
                .stream().findFirst().orElseThrow(() -> new SpaRoomNotFoundException("No available rooms"));

        SpaStaff staff;
        if (dto.getStaffId() != null) {
            staff = spaStaffService.getEntityById(dto.getStaffId());
            List<SpaStaff> available = spaStaffService.getAvailableEntities(dto.getBookingDate(), dto.getStartTime(), endTime);
            UUID staffId = staff.getId();

            if (available.stream().noneMatch(s -> s.getId().equals(staffId))) {
                throw new SpaStaffNotFoundException("Selected therapist is not available for this time");
            }
        } else {
            List<SpaStaff> available = spaStaffService.getAvailableEntities(dto.getBookingDate(), dto.getStartTime(), endTime);
            if (!available.isEmpty()) {
                staff = available.get(0);
            }else {
                throw new SpaStaffNotFoundException("No available staff for selected time");
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

        log.info("booking {} was created", booking.getId());

        NotificationRequest request = NotificationRequest.builder()
                .bookingId(booking.getId())
                .userId(userId)
                .email(userRepository.getEmailById(userId).orElseThrow(() -> new EmailNotPresent("Email was not found for user id: " + userId)))
                .phone(guest.getPhone())
                .confirmationCode(booking.getConfirmationCode())
                .customerName(guest.getFirstName() + " " + guest.getLastName())
                .appointmentTime(LocalDateTime.of(dto.getBookingDate(), dto.getStartTime()))
                .treatmentName(treatments.stream().map(SpaTreatment::getName).collect(Collectors.joining(", ")))
                .build();

        try {
            notificationService.sendConfirmation(request);
            log.info("call to notification service was made");
        } catch (Exception e) {
            log.error("Failed to call notification service for booking {}: {}", booking.getId(), e.getMessage());
        }
    }

    private List<String> generateSlots(LocalDate date) {
        List<String> slots = new ArrayList<>();
        LocalTime time;
        if (date.equals(LocalDate.now())){
            int minutes = LocalTime.now().getMinute();
            int remainder = minutes % 30;
            int add = (remainder == 0) ? 0 : 30 - remainder;
            time = LocalTime.now().truncatedTo(ChronoUnit.MINUTES).plusMinutes(add);
        }else {
            time = workingHours(date).get(0);
        }
        LocalTime closing = workingHours(date).get(1);

        while (time.isBefore(closing.minusMinutes(30))) {
            slots.add(time.toString());
            time = time.plusMinutes(30);
        }
        return slots;
    }

    private String generateConfirmationCode(){
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 10);
    }

    @Scheduled(cron = "0 0 23 * * MON-SAT")
    public void setStatusCompleted(){
        int updated = spaBookingRepository.updatePastBookings(BookingStatus.PENDING,
                BookingStatus.COMPLETED, LocalDate.now());

        log.info("Changed {} to completed", updated);
    }
}
