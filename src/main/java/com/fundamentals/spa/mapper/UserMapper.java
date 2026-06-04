package com.fundamentals.spa.Mapper;

import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.User;
import com.fundamentals.spa.entity.enums.UserRole;
import lombok.*;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {
    public User toEntity(RegisterDto dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(UserRole.CLIENT);
        return user;
    }

    public RegisterDto ToDto(User user){
        return RegisterDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
