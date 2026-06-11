package com.fundamentals.spa.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpaStaffDto {
    private String specialization;
    private String firstName;
    private String lastName;
}
