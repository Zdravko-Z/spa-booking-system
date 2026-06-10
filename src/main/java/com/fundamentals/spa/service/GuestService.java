package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.GuestDto;
import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.Guest;
import com.fundamentals.spa.entity.User;
import com.fundamentals.spa.exception.GuestNotFoundException;
import com.fundamentals.spa.mapper.GuestMapper;
import com.fundamentals.spa.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestService {
    private final GuestRepository guestRepository;

    public void createGuest(RegisterDto registerDto, User user){
        Guest guest = GuestMapper.toGuest(registerDto, user);
        guestRepository.save(guest);
    }

    public GuestDto getById(UUID id){
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found"));
        return GuestMapper.toDto(guest);
    }

    public void updateGuestProfile(GuestDto dto){
        Guest guest = guestRepository.findById(dto.getUser().getId())
                .orElseThrow(() -> new GuestNotFoundException("Guest not found"));

        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setPhone(dto.getPhone());
    }

    public Guest getByUser(UUID user) {
        return guestRepository.getByUserId(user).orElseThrow(() -> new GuestNotFoundException("Guest not found"));
    }
}
