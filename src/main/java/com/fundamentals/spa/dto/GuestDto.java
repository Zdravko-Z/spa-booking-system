package com.fundamentals.spa.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class GuestDto {
    private String firstName;
    private String lastName;
    private String phone;
    private UUID userId;
}
