package com.proyectapi.tech_suport.implementations;

import com.proyectapi.tech_suport.employee.EmployeeDTO;
import com.proyectapi.tech_suport.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeService;

    private EmployeeDTO employee;

    @BeforeEach
    void setUp() {
        // Limpiamos la base de datos
        employeeRepository.deleteAll();

        employee = new EmployeeDTO();
        employee.setName("Juan Perez");
        employee.setEmail("juan@example.com");

        employee = employeeService.createEmployee(employee);
    }

    @Test
    void testCreateEmployee() {
        EmployeeDTO newEmployee = new EmployeeDTO();
        newEmployee.setName("Ana Lopez");
        newEmployee.setEmail("ana@example.com");

        EmployeeDTO saved = employeeService.createEmployee(newEmployee);

        assertNotNull(saved.getId());
        assertEquals("Ana Lopez", saved.getName());
        assertEquals("ana@example.com", saved.getEmail());
    }

    @Test
    void testGetEmployeeById() {
        EmployeeDTO found = employeeService.getEmployeeById(employee.getId());
        assertEquals(employee.getName(), found.getName());
        assertEquals(employee.getEmail(), found.getEmail());
    }

    @Test
void testGetEmployeeById_NotFound() {
    assertThrows(ResponseStatusException.class, () ->
            employeeService.getEmployeeById(999L));
}

    @Test
    void testUpdateEmployee() {
        EmployeeDTO update = new EmployeeDTO();
        update.setName("Carlos Gomez");
        update.setEmail("carlos@example.com");

        EmployeeDTO updated = employeeService.updateEmployee(employee.getId(), update);

        assertEquals("Carlos Gomez", updated.getName());
        assertEquals("carlos@example.com", updated.getEmail());
    }

    @Test
    void testDeleteEmployee() {
        employeeService.deleteEmployee(employee.getId());
        Optional<EmployeeDTO> deleted = employeeService.findByIdOptional(employee.getId());
        assertTrue(deleted.isEmpty());
    }
}