package com.fundamentals.spa.service;
import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.Guest;
import com.fundamentals.spa.entity.User;
import com.fundamentals.spa.exception.GuestNotFoundException;
import com.fundamentals.spa.exception.SpaUserNotFound;
import com.fundamentals.spa.mapper.GuestMapper;
import com.fundamentals.spa.repository.GuestRepository;
import com.fundamentals.spa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestService {
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;

    public void createGuest(RegisterDto registerDto, UUID id){
        User user = userRepository.findById(id).orElseThrow(() -> new SpaUserNotFound("User not found"));
        Guest guest = GuestMapper.toGuest(registerDto, user);
        guestRepository.save(guest);
    }

    public Guest getByUser(UUID user) {
        return guestRepository.findByUser_Id(user).orElseThrow(() -> new GuestNotFoundException("Guest not found"));
    }
}
