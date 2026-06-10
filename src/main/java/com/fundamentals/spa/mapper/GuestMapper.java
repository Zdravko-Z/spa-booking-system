package com.fundamentals.spa.mapper;

import com.fundamentals.spa.dto.GuestDto;
import com.fundamentals.spa.dto.RegisterDto;
import com.fundamentals.spa.entity.Guest;
import com.fundamentals.spa.entity.User;

public class GuestMapper {

    public static Guest toGuest(RegisterDto registerDto, User user){
        Guest guest = new Guest();
        guest.setFirstName(registerDto.getFirstName());
        guest.setLastName(registerDto.getLastName());
        guest.setPhone(registerDto.getPhone());
        guest.setUser(user);

        return guest;
    }

    public static GuestDto toDto(Guest guest){
//        GuestDto dto = new GuestDto();
        return GuestDto.builder()
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .phone(guest.getPhone())
                .user(guest.getUser())
                .build();
    }
}
