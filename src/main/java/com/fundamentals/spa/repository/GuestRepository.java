package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestRepository extends JpaRepository<Guest, UUID> {

    Optional<Guest> findByUser_Id(UUID id);
}
