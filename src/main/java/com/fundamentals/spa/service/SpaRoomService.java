package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.SpaRoomDto;
import com.fundamentals.spa.entity.SpaRoom;
import com.fundamentals.spa.exception.SpaRoomNotFoundException;
import com.fundamentals.spa.mapper.SpaRoomMapper;
import com.fundamentals.spa.repository.SpaRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpaRoomService {
    private final SpaRoomRepository spaRoomRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "rooms")
    public List<SpaRoomDto> getAll(){
        return spaRoomRepository.findAll().stream().map(SpaRoomMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "rooms")
    public SpaRoomDto getById(UUID id){
        SpaRoomDto room = SpaRoomMapper.toDto(spaRoomRepository.findById(id).orElseThrow(() -> new SpaRoomNotFoundException("Room not found")));
        log.info("room {} was found", room.getId());
        return room;
    }

    @CacheEvict(value = "rooms", allEntries = true)
    public void update(SpaRoomDto dto, UUID id){
        SpaRoom room = spaRoomRepository.findById(id).orElseThrow(() -> new SpaRoomNotFoundException("Room not found"));

        room.setCapacity(dto.getCapacity());
        room.setStatus(dto.getStatus());
        room.setName(dto.getName());

        log.info("room {} was updated", id);
    }

    public List<SpaRoom> getAvailableRoom(LocalDate bookingDate, LocalTime startTime, LocalTime endTime) {
        List<SpaRoom> rooms =  spaRoomRepository.getAllAvailableRooms(bookingDate, startTime, endTime)
                .stream().toList();

        if (rooms.isEmpty()){
            log.info("no available rooms were found");
        }else {
            log.info("there are available rooms to book");
        }
        return rooms;
    }
}
