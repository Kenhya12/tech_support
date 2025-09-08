package com.proyectapi.tech_suport.implementations;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectapi.tech_suport.employee.EmployeeDTO;
import com.proyectapi.tech_suport.employee.EmployeeEntity;
import com.proyectapi.tech_suport.employee.EmployeeMapper;
import com.proyectapi.tech_suport.request.EmployeeRepository;
import com.proyectapi.tech_suport.service.EmployeeWriteService;
import com.proyectapi.tech_suport.service.EmployeeReadService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeReadService, EmployeeWriteService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    @Override
public EmployeeDTO getEmployeeById(Long id) {
    EmployeeEntity entity = employeeRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    return mapper.toDTO(entity);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        EmployeeEntity entity = mapper.toEntity(dto);
        EmployeeEntity saved = employeeRepository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        EmployeeEntity entity = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setDepartment(dto.getDepartment());
        return mapper.toDTO(employeeRepository.save(entity));
    }

    @Override
    public void deleteEmployee(Long id) {
        EmployeeEntity entity = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(entity);
    }
}