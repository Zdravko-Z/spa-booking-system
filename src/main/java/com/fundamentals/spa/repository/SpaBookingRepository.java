package com.fundamentals.spa.repository;

import com.fundamentals.spa.dto.AllBookings;
import com.fundamentals.spa.entity.Guest;
import com.fundamentals.spa.entity.SpaBooking;
import com.fundamentals.spa.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpaBookingRepository extends JpaRepository<SpaBooking, UUID> {
    @Query("""
            SELECT new com.fundamentals.spa.dto.AllBookings(
                b.id, b.confirmationCode, b.bookingDate, b.startTime, b.endTime,
                b.totalPrice, b.status,
                b.notes, b.durationMinutes,
                g.firstName, g.lastName,
                r.name,
                s.firstName, s.lastName
            )
            FROM SpaBooking b
            JOIN b.guest g
            JOIN g.user
            JOIN b.spaRoom r
            LEFT JOIN b.staff s
            LEFT JOIN s.user
            """)
    List<AllBookings> findBookingSummaries();

    @Query("""
                SELECT b.startTime FROM SpaBooking b
                WHERE b.bookingDate = :bookingDate
                AND b.status <> com.fundamentals.spa.entity.enums.BookingStatus.CANCELLED
            """)
    List<LocalTime> findTakenSlots(@Param("bookingDate") LocalDate bookingDate);

    @Modifying(clearAutomatically = true)
    @Query("""
                    UPDATE SpaBooking b SET b.status = :newStatus
                    WHERE b.status = :oldStatus AND b.bookingDate <= :today
            """)
    int updatePastBookings(@Param("oldStatus") BookingStatus oldStatus,
                           @Param("newStatus") BookingStatus newStatus,
                           @Param("today") LocalDate today);

    @Modifying
    @Query("""
            UPDATE SpaBooking b SET b.status = :newStatus
            WHERE b.status = :oldStatus AND b.bookingDate = :today AND b.endTime < :now
            """)
    int updateTodayCompletedBookings(@Param("oldStatus") BookingStatus oldStatus,
                                      @Param("newStatus") BookingStatus newStatus,
                                      @Param("today") LocalDate today,
                                      @Param("now") LocalTime now);

    @Query("""
    SELECT new com.fundamentals.spa.dto.AllBookings(
        b.id, b.confirmationCode, b.bookingDate, b.startTime, b.endTime,
        b.totalPrice, b.status,
        b.notes, b.durationMinutes,
        g.firstName, g.lastName,
        r.name,
        s.firstName, s.lastName
    )
    FROM SpaBooking b
    JOIN b.guest g
    JOIN b.spaRoom r
    LEFT JOIN b.staff s
    WHERE b.guest = :guest
    """)
    List<AllBookings> findSummariesByGuest(Guest guest);

}
