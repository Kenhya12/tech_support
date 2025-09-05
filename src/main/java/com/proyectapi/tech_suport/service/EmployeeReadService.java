package com.proyectapi.tech_suport.service;


import java.util.List;

import com.proyectapi.tech_suport.employee.EmployeeDTO;

public interface EmployeeReadService {
    EmployeeDTO getEmployeeById(Long id);
    List<EmployeeDTO> getAllEmployees();
}