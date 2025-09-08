package com.proyectapi.tech_suport.employee;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeDTOTest {

    @Test
    void testGettersAndSetters() {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");

        assertEquals(1L, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals("john.doe@example.com", employee.getEmail());
        assertEquals("IT", employee.getDepartment());
    }

    @Test
    void testBuilder() {
        EmployeeDTO employee = EmployeeDTO.builder()
                .id(2L)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .department("HR")
                .build();

        assertEquals(2L, employee.getId());
        assertEquals("Jane Smith", employee.getName());
        assertEquals("jane.smith@example.com", employee.getEmail());
        assertEquals("HR", employee.getDepartment());
    }
}