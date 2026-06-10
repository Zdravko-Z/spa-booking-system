package com.fundamentals.spa.mapper;

import com.fundamentals.spa.dto.LoginDto;
import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.User;
import com.fundamentals.spa.entity.enums.UserRole;
import lombok.*;

public class UserMapper {
    public static User toEntity(RegisterDto dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(UserRole.CLIENT);
        return user;
    }

    public static RegisterDto toRegisterDto(User user){
        return RegisterDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public static LoginDto toLoginDto(User user){
        return LoginDto.builder()
                .username(user.getUsername())
                .build();
    }
}
