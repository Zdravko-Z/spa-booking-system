package com.fundamentals.spa.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {
    private String username;
    private String password;
}
