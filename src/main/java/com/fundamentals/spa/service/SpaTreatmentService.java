package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.SpaRoomDto;
import com.fundamentals.spa.dto.SpaTreatmentDto;
import com.fundamentals.spa.entity.SpaRoom;
import com.fundamentals.spa.entity.SpaTreatment;
import com.fundamentals.spa.exception.SpaTreatmentNotFoundException;
import com.fundamentals.spa.mapper.SpaRoomMapper;
import com.fundamentals.spa.mapper.SpaTreatmentMapper;
import com.fundamentals.spa.repository.SpaTreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SpaTreatmentService {
    private final SpaTreatmentRepository spaTreatmentRepository;

    public void create(SpaTreatmentDto dto) {
        SpaTreatment spaTreatment = SpaTreatmentMapper.toEntity(dto);
        spaTreatmentRepository.save(spaTreatment);
    }

    @Transactional(readOnly = true)
    public SpaTreatmentDto getById(UUID id) {
        SpaTreatment spaTreatment = spaTreatmentRepository.findById(id)
                .orElseThrow(() -> new SpaTreatmentNotFoundException("Treatment not found"));

        return SpaTreatmentMapper.toDto(spaTreatment);
    }

    @Transactional(readOnly = true)
    public List<SpaTreatmentDto> getAll() {
        return spaTreatmentRepository.findAll().stream().map(SpaTreatmentMapper::toDto).toList();
    }

    public void update(SpaTreatmentDto dto, UUID id) {
        SpaTreatment treatment = spaTreatmentRepository.findById(id)
                .orElseThrow(() -> new SpaTreatmentNotFoundException("Treatment not found"));

        treatment.setName(dto.getName());
        treatment.setPrice(dto.getPrice());
        treatment.setDurationMinutes(dto.getDurationMinutes());
        treatment.setDescription(dto.getDescription());
    }

    public void delete(UUID id) {
        if (!spaTreatmentRepository.existsById(id)){
            throw new SpaTreatmentNotFoundException("Treatment not found");
        }
        spaTreatmentRepository.deleteById(id);
    }
}
