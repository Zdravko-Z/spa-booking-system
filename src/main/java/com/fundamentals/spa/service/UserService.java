package com.fundamentals.spa.service;

import com.fundamentals.spa.exception.EmailAlreadyUsedException;
import com.fundamentals.spa.exception.SpaException;
import com.fundamentals.spa.exception.SpaUserNotFound;
import com.fundamentals.spa.mapper.UserMapper;
import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.User;
import com.fundamentals.spa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new SpaUserNotFound("User not found"));
    }

    public UUID register(RegisterDto dto){
        if (userRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyUsedException("Email is already used");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new SpaException("Passwords do not match");
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        log.info("User registered {}", dto.getId());
        return user.getId();
    }
}
