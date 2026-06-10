package com.fundamentals.spa.mapper;

import com.fundamentals.spa.dto.SpaRoomDto;
import com.fundamentals.spa.entity.SpaRoom;

public class SpaRoomMapper {
    public static SpaRoom toEntity(SpaRoomDto dto){
        SpaRoom room = new SpaRoom();
        room.setName(dto.getName());
        room.setStatus(dto.getStatus());
        room.setCapacity(dto.getCapacity());
        return room;
    }

    public static SpaRoomDto toDto(SpaRoom room){
        return SpaRoomDto.builder()
                .name(room.getName())
                .capacity(room.getCapacity())
                .status(room.getStatus())
                .build();
    }
}
