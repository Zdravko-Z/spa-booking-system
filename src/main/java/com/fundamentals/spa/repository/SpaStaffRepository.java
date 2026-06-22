package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.SpaStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpaStaffRepository extends JpaRepository<SpaStaff, UUID> {
    @Query("""
            SELECT s FROM SpaStaff s
            WHERE s NOT IN(
                        SELECT b.staff FROM SpaBooking b
                        WHERE b.bookingDate = :bookingDate
                        AND b.startTime < :endTime
                        AND b.endTime > :startTime
                        AND b.status <> 'CANCELLED'
                )
            """)
    List<SpaStaff> getAllAvailable(@Param("bookingDate") LocalDate bookingDate,
                                   @Param("startTime") LocalTime startDate,
                                   @Param("endTime") LocalTime endTime);
}
