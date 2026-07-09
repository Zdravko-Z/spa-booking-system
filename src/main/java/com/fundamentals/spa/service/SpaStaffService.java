package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.SpaStaffDto;
import com.fundamentals.spa.entity.SpaStaff;
import com.fundamentals.spa.exception.SpaStaffNotFoundException;
import com.fundamentals.spa.mapper.SpaStaffMapper;
import com.fundamentals.spa.repository.SpaStaffRepository;
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
@Transactional
@RequiredArgsConstructor
public class SpaStaffService {
    private final SpaStaffRepository staffRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "staff")
    public SpaStaffDto getById(UUID id) {
        SpaStaff staff = staffRepository.findById(id)
                .orElseThrow(() -> new SpaStaffNotFoundException("Staff not found"));
        return SpaStaffMapper.toDto(staff);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "staff")
    public List<SpaStaffDto> getAll(){
        return staffRepository.findAll().stream().map(SpaStaffMapper::toDto).toList();
    }

    @CacheEvict(value = "staff", allEntries = true)
    public void update(UUID id, SpaStaffDto dto){
        SpaStaff staff = staffRepository.findById(id).orElseThrow(() -> new SpaStaffNotFoundException("Staff member not found"));

        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setSpecialization(dto.getSpecialization());

        log.info("Staff {} updated", id);
    }

    @Transactional(readOnly = true)
    public List<SpaStaff> getAvailableEntities(LocalDate date, LocalTime start, LocalTime end) {
        return staffRepository.getAllAvailable(date, start, end);
    }

    @Cacheable(value = "staff")
    public SpaStaff getEntityById(UUID id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new SpaStaffNotFoundException("Staff not found"));
    }
}
