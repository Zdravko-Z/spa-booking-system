package com.fundamentals.spa.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SpaTreatmentDto {
    private String name;
    private String description;
    private int durationMinutes;
    private BigDecimal price;
}
