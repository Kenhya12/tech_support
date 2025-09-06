package com.proyectapi.tech_suport.service;

import com.proyectapi.tech_suport.employee.EmployeeDTO;
import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employee);
    EmployeeDTO getEmployeeById(Long id);
    List<EmployeeDTO> getAllEmployees();       
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDetails);
    void deleteEmployee(Long id);
}
