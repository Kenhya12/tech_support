package com.proyectapi.tech_suport.request;

import com.proyectapi.tech_suport.employee.EmployeeEntity;
import com.proyectapi.tech_suport.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository; // usar directamente para insertar datos

    private EmployeeEntity emp1;
    private EmployeeEntity emp2;

    @BeforeEach
    void setUp() {
        // Limpiar tabla antes de cada test
        employeeRepository.deleteAll();

        // Crear empleados de prueba directamente con la entidad
        emp1 = EmployeeEntity.builder()
                .name("Paula")
                .email("paula@example.com")
                .department("IT")
                .build();

        emp2 = EmployeeEntity.builder()
                .name("Luis")
                .email("luis@example.com")
                .department("HR")
                .build();

        // Guardar en la base de datos H2
        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
    }

    @Test
    void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Paula"))
                .andExpect(jsonPath("$[1].name").value("Luis"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        Long id = employeeRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/v1/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paula"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        String json = """
                {
                    "name": "Carlos",
                    "email": "carlos@example.com",
                    "department": "Finance"
                }
                """;

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@example.com"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Long id = employeeRepository.findAll().get(0).getId();
        String json = """
                {
                    "name": "Paula Updated",
                    "email": "paula_updated@example.com",
                    "department": "IT"
                }
                """;

        mockMvc.perform(put("/api/v1/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paula Updated"))
                .andExpect(jsonPath("$.email").value("paula_updated@example.com"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Long id = employeeRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/v1/employees/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetEmployeeNotFound() throws Exception {
        Long missingId = 999L;

        mockMvc.perform(get("/api/v1/employees/{id}", missingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
