package com.fundamentals.spa.mapper;

import com.fundamentals.spa.dto.SpaStaffDto;
import com.fundamentals.spa.entity.SpaStaff;

public class SpaStaffMapper {
    public static SpaStaff toEntity(SpaStaffDto dto){
        SpaStaff staff = new SpaStaff();
        staff.setSpecialization(dto.getSpecialization());
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());

        return staff;
    }

    public static SpaStaffDto toDto(SpaStaff staff){
        return SpaStaffDto.builder()
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .specialization(staff.getSpecialization())
                .build();
    }
}
