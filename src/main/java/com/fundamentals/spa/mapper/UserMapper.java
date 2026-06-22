package com.fundamentals.spa.mapper;

import com.fundamentals.spa.dto.AuthDto;
import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.User;
import com.fundamentals.spa.entity.enums.UserRole;

public class UserMapper {
    public static User toEntity(RegisterDto dto){
        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail().trim());
        user.setRole(UserRole.CLIENT);
        return user;
    }

    public static AuthDto toAuthDto(User user){
        return AuthDto.builder()
                .id(user.getId())
                .role(user.getRole())
                .build();
    }
}
