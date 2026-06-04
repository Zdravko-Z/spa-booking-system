package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.SpaBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpaBookingRepository extends JpaRepository<SpaBooking, UUID> {
}
