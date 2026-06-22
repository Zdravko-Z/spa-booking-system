package com.fundamentals.spa.mapper;

import com.fundamentals.spa.dto.SpaTreatmentDto;
import com.fundamentals.spa.entity.SpaTreatment;


public class SpaTreatmentMapper {
    public static SpaTreatmentDto toDto(SpaTreatment treatment){
        return SpaTreatmentDto.builder()
                .id(treatment.getId())
                .name(treatment.getName())
                .description(treatment.getDescription())
                .durationMinutes(treatment.getDurationMinutes())
                .price(treatment.getPrice())
                .deleted(treatment.isDeleted())
                .build();
    }

    public static SpaTreatment toEntity(SpaTreatmentDto dto){
        SpaTreatment spaTreatment = new SpaTreatment();
        spaTreatment.setName(dto.getName().trim());
        spaTreatment.setDescription(dto.getDescription().trim());
        spaTreatment.setDurationMinutes(dto.getDurationMinutes());
        spaTreatment.setPrice(dto.getPrice());

        return spaTreatment;
    }
}
