package com.fundamentals.spa.dto;

import com.fundamentals.spa.entity.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class AuthDto {
    private UUID id;
    private UserRole role;
}
