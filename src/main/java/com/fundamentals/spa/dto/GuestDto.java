package com.fundamentals.spa.dto;

import com.fundamentals.spa.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GuestDto {

    private String firstName;
    private String lastName;
    private String phone;
    private User user;
}
