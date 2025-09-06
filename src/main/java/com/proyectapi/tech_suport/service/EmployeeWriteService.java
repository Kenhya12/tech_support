package com.proyectapi.tech_suport.service;

import com.proyectapi.tech_suport.employee.EmployeeDTO;

public interface EmployeeWriteService {
    EmployeeDTO createEmployee(EmployeeDTO dto);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO dto);
    void deleteEmployee(Long id);
}