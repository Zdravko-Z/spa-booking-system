package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.SpaRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpaRoomRepository extends JpaRepository<SpaRoom, UUID> {
    @Query("""
    SELECT r FROM SpaRoom r
    WHERE r.status = 'AVAILABLE'
    AND r NOT IN (
        SELECT b.spaRoom FROM SpaBooking b
        WHERE b.bookingDate = :bookingDate
        AND b.startTime < :endTime
        AND b.endTime > :startTime
        AND b.status <> com.fundamentals.spa.entity.enums.BookingStatus.CANCELLED
    )
""")
    List<SpaRoom> getAllAvailableRooms(@Param("bookingDate") LocalDate bookingDate,
                                       @Param("startTime") LocalTime startTime,
                                       @Param("endTime") LocalTime endTime);
}
