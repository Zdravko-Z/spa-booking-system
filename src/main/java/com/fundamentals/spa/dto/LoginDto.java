package com.fundamentals.spa.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String username;
    private String password;
}
