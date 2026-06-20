package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.SpaTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpaTreatmentRepository extends JpaRepository<SpaTreatment, UUID> {
    List<SpaTreatment> findAllByDeletedFalse();
}
