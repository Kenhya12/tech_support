package com.proyectapi.tech_suport.controller;

import com.proyectapi.tech_suport.employee.EmployeeDTO;
import com.proyectapi.tech_suport.service.EmployeeReadService;
import com.proyectapi.tech_suport.service.EmployeeWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeReadService employeeReadService;
    private final EmployeeWriteService employeeWriteService;

    @Autowired
    public EmployeeController(EmployeeReadService employeeReadService, EmployeeWriteService employeeWriteService) {
        this.employeeReadService = employeeReadService;
        this.employeeWriteService = employeeWriteService;
    }

    // GET /employees
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeReadService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // POST /employees
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employee) {
        EmployeeDTO saved = employeeWriteService.createEmployee(employee);
        return ResponseEntity.ok(saved);
    }

    // GET /employees/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeDTO employee = employeeReadService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            // Traducimos RuntimeException a HTTP 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // PUT /employees/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDetails) {
        EmployeeDTO updated = employeeWriteService.updateEmployee(id, employeeDetails);
        return ResponseEntity.ok(updated);
    }

    // DELETE /employees/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeWriteService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
