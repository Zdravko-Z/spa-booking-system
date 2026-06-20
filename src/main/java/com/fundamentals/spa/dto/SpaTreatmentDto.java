package com.fundamentals.spa.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaTreatmentDto {
    private UUID id;
    private String name;
    private String description;
    private int durationMinutes;
    private BigDecimal price;
    private boolean deleted;
}
