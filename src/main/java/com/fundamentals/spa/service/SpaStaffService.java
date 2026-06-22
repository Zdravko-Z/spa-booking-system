package com.fundamentals.spa.service;

import com.fundamentals.spa.dto.SpaStaffDto;
import com.fundamentals.spa.entity.SpaStaff;
import com.fundamentals.spa.exception.SpaStaffNotFoundException;
import com.fundamentals.spa.mapper.SpaStaffMapper;
import com.fundamentals.spa.repository.SpaStaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SpaStaffService {
    private final SpaStaffRepository staffRepository;

    @Transactional(readOnly = true)
    public SpaStaffDto getById(UUID id) {
        SpaStaff staff = staffRepository.findById(id)
                .orElseThrow(() -> new SpaStaffNotFoundException("Staff not found"));
        return SpaStaffMapper.toDto(staff);
    }

    @Transactional(readOnly = true)
    public List<SpaStaffDto> getAll(){
        return staffRepository.findAll().stream().map(SpaStaffMapper::toDto).toList();
    }

    public void update(UUID id, SpaStaffDto dto){
        SpaStaff staff = staffRepository.findById(id).orElseThrow(() -> new SpaStaffNotFoundException("Staff member not found"));

        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setSpecialization(dto.getSpecialization());
    }

    @Transactional(readOnly = true)
    public List<SpaStaffDto> getAllAvailable(LocalDate date, LocalTime start, LocalTime end) {
        return staffRepository.getAllAvailable(date, start, end).stream().map(SpaStaffMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<SpaStaff> getAvailableEntities(LocalDate date, LocalTime start, LocalTime end) {
        return staffRepository.getAllAvailable(date, start, end);
    }

    public SpaStaff getEntityById(UUID id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new SpaStaffNotFoundException("Staff not found"));
    }
}
