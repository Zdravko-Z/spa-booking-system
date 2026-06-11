package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.SpaRoomDto;
import com.fundamentals.spa.entity.SpaRoom;
import com.fundamentals.spa.entity.enums.RoomStatus;
import com.fundamentals.spa.exception.SpaRoomNotFoundException;
import com.fundamentals.spa.mapper.SpaRoomMapper;
import com.fundamentals.spa.repository.SpaRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SpaRoomService {
    private final SpaRoomRepository spaRoomRepository;

    @Transactional(readOnly = true)
    public List<SpaRoomDto> getAll(){
        return spaRoomRepository.findAll().stream().map(SpaRoomMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<SpaRoomDto> getAllWithStatus(String status){
        return spaRoomRepository.getAllByStatus(RoomStatus.valueOf(status.toUpperCase())).stream().map(SpaRoomMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SpaRoomDto room(UUID id){
        return SpaRoomMapper.toDto(spaRoomRepository.findById(id).orElseThrow(() -> new SpaRoomNotFoundException("Room not found")));
    }

    public void update(SpaRoomDto dto, UUID id){
        SpaRoom room = spaRoomRepository.findById(id).orElseThrow(() -> new SpaRoomNotFoundException("Room not found"));

        room.setCapacity(dto.getCapacity());
        room.setStatus(dto.getStatus());
        room.setName(dto.getName());
    }

    public List<SpaRoom> getAvailableRoom(LocalDate bookingDate, LocalTime startTime, LocalTime endTime) {
        return spaRoomRepository.getAllAvailableRooms(bookingDate, startTime, endTime)
                .stream().toList();
    }
}
