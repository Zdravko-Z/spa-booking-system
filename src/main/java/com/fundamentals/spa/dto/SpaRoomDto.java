package com.fundamentals.spa.dto;

import com.fundamentals.spa.entity.enums.RoomStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Builder
@Getter
public class SpaRoomDto {
    private UUID id;

    private String roomNumber;

    @NotBlank(message = "Room name is required")
    @Size(min = 2, max = 100, message = "Room name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 20, message = "Capacity cannot exceed 20")
    private int capacity;

    @NotNull(message = "Status is required")
    private RoomStatus status;
}
