package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.SpaStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpaStaffRepository extends JpaRepository<SpaStaff, UUID> {
}
