package com.proyectapi.tech_suport.employee;


public interface EmployeeWriteService {
    EmployeeDTO createEmployee(EmployeeDTO dto);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO dto);
    void deleteEmployee(Long id);
}