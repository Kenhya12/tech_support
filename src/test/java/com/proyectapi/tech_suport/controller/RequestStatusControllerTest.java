package com.proyectapi.tech_suport.controller;

import com.proyectapi.tech_suport.request.RequestStatusEntity;
import com.proyectapi.tech_suport.repository.RequestStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
class RequestStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestStatusRepository requestStatusRepository;

    @BeforeEach
    void setUp() {
        requestStatusRepository.deleteAll(); // Limpiar datos previos

        RequestStatusEntity status1 = new RequestStatusEntity();
        status1.setName("PENDING");

        RequestStatusEntity status2 = new RequestStatusEntity();
        status2.setName("RESOLVED");

        requestStatusRepository.saveAll(List.of(status1, status2));
    }

    @Test
    void testGetAllStatuses() throws Exception {
        mockMvc.perform(get("/api/v1/request-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("PENDING"))
                .andExpect(jsonPath("$[1].name").value("RESOLVED"));
    }

    @Test
    void testGetStatusById() throws Exception {
        // Obtener un ID existente
        RequestStatusEntity existingStatus = requestStatusRepository.findAll().get(0);

        // Test endpoint GET /api/v1/request-status/{id}
        mockMvc.perform(get("/api/v1/request-status/{id}", existingStatus.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(existingStatus.getName()));
    }

    @Test
    void testGetStatusByIdNotFound() throws Exception {
        // Usar un ID que no existe
        Long nonExistentId = 999L;

        mockMvc.perform(get("/api/v1/request-status/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateStatus() throws Exception {
        String newStatusJson = """
                {
                    "name": "IN_PROGRESS"
                }
                """;

        mockMvc.perform(post("/api/v1/request-status")
                .contentType("application/json")
                .content(newStatusJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IN_PROGRESS"));
    }

    @Test
    void testUpdateStatus() throws Exception {
        // Obtener un ID existente
        RequestStatusEntity existingStatus = requestStatusRepository.findAll().get(0);

        String updatedStatusJson = """
                {
                    "name": "UPDATED_STATUS"
                }
                """;

        mockMvc.perform(put("/api/v1/request-status/{id}", existingStatus.getId())
                .contentType("application/json")
                .content(updatedStatusJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UPDATED_STATUS"));
    }

    @Test
    void testDeleteStatus() throws Exception {
        // Obtener un ID existente
        RequestStatusEntity existingStatus = requestStatusRepository.findAll().get(0);

        mockMvc.perform(delete("/api/v1/request-status/{id}", existingStatus.getId()))
                .andExpect(status().isOk());

        // Verificar que el status ya no existe
        mockMvc.perform(get("/api/v1/request-status/{id}", existingStatus.getId()))
                .andExpect(status().isNotFound());
    }
}
