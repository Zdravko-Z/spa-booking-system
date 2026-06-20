package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.Guest;
import com.fundamentals.spa.entity.SpaBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpaBookingRepository extends JpaRepository<SpaBooking, UUID> {

    List<SpaBooking> getAllByGuest(Guest guest);

    @Query("""
                SELECT b.startTime FROM SpaBooking b
                WHERE b.bookingDate = :bookingDate
                AND b.status <> com.fundamentals.spa.entity.enums.BookingStatus.CANCELLED
            """)
    List<LocalTime> findTakenSlots(@Param("bookingDate") LocalDate bookingDate);
}
