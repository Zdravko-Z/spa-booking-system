package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.SpaTreatmentDto;
import com.fundamentals.spa.entity.SpaTreatment;
import com.fundamentals.spa.exception.SpaTreatmentNotFoundException;
import com.fundamentals.spa.mapper.SpaTreatmentMapper;
import com.fundamentals.spa.repository.SpaBookingTreatmentRepository;
import com.fundamentals.spa.repository.SpaTreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SpaTreatmentService {
    private final SpaTreatmentRepository spaTreatmentRepository;
    private final SpaBookingTreatmentRepository spaBookingTreatmentRepository;

    @CacheEvict(value = "treatments", allEntries = true)
    public void create(SpaTreatmentDto dto) {
        SpaTreatment spaTreatment = SpaTreatmentMapper.toEntity(dto);
        spaTreatmentRepository.save(spaTreatment);
    }

    @Transactional(readOnly = true)
    public SpaTreatment getEntityById(UUID id) {
        return spaTreatmentRepository.findById(id)
                .orElseThrow(() -> new SpaTreatmentNotFoundException("Treatment not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable("treatments")
    public List<SpaTreatmentDto> getAllNotDeleted() {
        return spaTreatmentRepository.findAllByDeletedFalse()
                .stream()
                .map(SpaTreatmentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SpaTreatmentDto getById(UUID id) {
        return spaTreatmentRepository.findById(id).map(SpaTreatmentMapper::toDto)
                .orElseThrow(() -> new SpaTreatmentNotFoundException("Treatment not found"));
    }

    @Cacheable("treatments")
    @Transactional(readOnly = true)
    public List<SpaTreatmentDto> getAll() {
        return spaTreatmentRepository.findAll().stream().map(SpaTreatmentMapper::toDto).toList();
    }

    @CacheEvict(value = "treatments", allEntries = true)
    public void update(SpaTreatmentDto dto, UUID id) {
        SpaTreatment treatment = spaTreatmentRepository.findById(id)
                .orElseThrow(() -> new SpaTreatmentNotFoundException("Treatment not found"));

        treatment.setName(dto.getName());
        treatment.setPrice(dto.getPrice());
        treatment.setDurationMinutes(dto.getDurationMinutes());
        treatment.setDescription(dto.getDescription());
    }

    @CacheEvict(value = "treatments", allEntries = true)
    public void delete(UUID id) {
        if (!spaTreatmentRepository.existsById(id)){
            throw new SpaTreatmentNotFoundException("Treatment not found");
        }

        if (spaBookingTreatmentRepository.existsByTreatmentId(id)){
            SpaTreatment treatment = spaTreatmentRepository.findById(id)
                    .orElseThrow(() -> new SpaTreatmentNotFoundException("Treatment not found"));
            treatment.setDeleted(true);
        }else {
            spaTreatmentRepository.deleteById(id);
        }
    }

    public void restore(UUID id) {
        SpaTreatment treatment = spaTreatmentRepository.findById(id)
                .orElseThrow(() -> new SpaTreatmentNotFoundException("Treatment not found"));
        treatment.setDeleted(false);
    }
}
