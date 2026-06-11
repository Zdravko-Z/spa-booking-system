package com.fundamentals.spa.dto;

import com.fundamentals.spa.entity.enums.RoomStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SpaRoomDto {
    private String roomNumber;
    private String name;
    private int capacity;
    private RoomStatus status;
}
