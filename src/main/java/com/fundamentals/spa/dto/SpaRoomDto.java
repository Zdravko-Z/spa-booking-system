package com.fundamentals.spa.dto;

import com.fundamentals.spa.entity.enums.RoomStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class SpaRoomDto {
    private UUID id;
    private String roomNumber;
    private String name;
    private int capacity;
    private RoomStatus status;
}
