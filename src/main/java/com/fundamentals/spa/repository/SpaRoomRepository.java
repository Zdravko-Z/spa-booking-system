package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.SpaRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpaRoomRepository extends JpaRepository<SpaRoom, UUID> {
}
