package com.proyectapi.tech_suport.employee;

import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDTO toDTO(EmployeeEntity entity) {
        if (entity == null) return null;
        return EmployeeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .department(entity.getDepartment())
                .build();
    }

    public EmployeeEntity toEntity(EmployeeDTO dto) {
        if (dto == null) return null;
        return EmployeeEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .build();
    }
}
