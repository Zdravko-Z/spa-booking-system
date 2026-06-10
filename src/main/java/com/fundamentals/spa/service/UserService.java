package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.LoginDto;
import com.fundamentals.spa.exception.EmailAlreadyUsedException;
import com.fundamentals.spa.exception.EmailNotPresent;
import com.fundamentals.spa.exception.UsernameOrPasswordMismatch;
import com.fundamentals.spa.mapper.UserMapper;
import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.User;
import com.fundamentals.spa.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GuestService guestService;

    @Transactional(readOnly = true)
    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotPresent("User not found"));
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void register(RegisterDto dto){
        if (userRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyUsedException("Email is already used");
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        guestService.createGuest(dto, user);
        userRepository.save(user);
    }

    public LoginDto login(LoginDto loginDto) {
        User foundUser = getByUsername(loginDto.getUsername());

        if (!passwordCheck(loginDto.getPassword(), foundUser.getPassword())){
            throw new UsernameOrPasswordMismatch("Username or password is wrong");
        }
        return UserMapper.toLoginDto(foundUser);
    }

    public boolean passwordCheck(String input,String password){
        return passwordEncoder.matches(input, password);
    }

    public User getById(UUID id) {
        return userRepository.getById(id);
    }
}
