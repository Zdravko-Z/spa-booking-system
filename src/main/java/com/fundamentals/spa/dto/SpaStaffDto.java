package com.fundamentals.spa.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpaStaffDto {
    private UUID id;
    private String specialization;
    private String firstName;
    private String lastName;
}
