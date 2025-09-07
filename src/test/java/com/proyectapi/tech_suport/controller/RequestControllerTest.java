package com.proyectapi.tech_suport.controller;

import com.proyectapi.tech_suport.request.RequestEntity;
import com.proyectapi.tech_suport.request.RequestStatusEntity;
import com.proyectapi.tech_suport.repository.RequestRepository;
import com.proyectapi.tech_suport.repository.RequestStatusRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestStatusRepository requestStatusRepository;

    private RequestStatusEntity pendingStatus;
    private RequestStatusEntity resolvedStatus;
    private RequestEntity request;

    @BeforeEach
    void setUp() {
        requestRepository.deleteAll();
        requestStatusRepository.deleteAll();

        pendingStatus = requestStatusRepository.findByName("PENDING")
                .orElseGet(() -> {
                    RequestStatusEntity status = new RequestStatusEntity();
                    status.setName("PENDING");
                    status.setCreatedAt(LocalDateTime.now());
                    status.setStatus("ACTIVE");
                    return requestStatusRepository.save(status);
                });

        resolvedStatus = requestStatusRepository.findByName("RESOLVED")
                .orElseGet(() -> {
                    RequestStatusEntity status = new RequestStatusEntity();
                    status.setName("RESOLVED");
                    status.setCreatedAt(LocalDateTime.now());
                    status.setStatus("ACTIVE");
                    return requestStatusRepository.save(status);
                });

        request = new RequestEntity();
        request.setDescription("Test request");
        request.setRequestStatus(pendingStatus);
        requestRepository.save(request);
    }

    @Test
    void testGetAllRequests() throws Exception {
        mockMvc.perform(get("/api/v1/requests").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test request"))
                .andExpect(jsonPath("$[0].requestStatus.name").value("PENDING"));
    }

    @Test
    void testGetAllRequestsEmpty() throws Exception {
        requestRepository.deleteAll();
        mockMvc.perform(get("/api/v1/requests").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testCreateRequest() throws Exception {
        String json = """
                {
                    "description": "New Test Request",
                    "requestStatusId": %d
                }
                """.formatted(pendingStatus.getId());

        mockMvc.perform(post("/api/v1/requests").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("New Test Request"))
                .andExpect(jsonPath("$.requestStatus.name").value("PENDING"));
    }

    @Test
    void testCreateRequestWithInvalidStatus() throws Exception {
        // Ensure invalid ID does not exist
        Long invalidStatusId = 999999L;
        while(requestStatusRepository.findById(invalidStatusId).isPresent()) {
            invalidStatusId++;
        }

        String json = """
                {
                    "description": "Invalid Status Request",
                    "requestStatusId": %d
                }
                """.formatted(invalidStatusId);

        mockMvc.perform(post("/api/v1/requests").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testUpdateRequest() throws Exception {
        String json = """
                {
                    "description": "Updated Request",
                    "requestStatusId": %d
                }
                """.formatted(pendingStatus.getId());

        mockMvc.perform(put("/api/v1/requests/{id}", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Request"))
                .andExpect(jsonPath("$.requestStatus.name").value(pendingStatus.getName()));
    }

    @Test
    void testUpdateRequestNonExistent() throws Exception {
        String json = """
                {
                    "description": "Updated Request",
                    "requestStatusId": %d
                }
                """.formatted(pendingStatus.getId());

        mockMvc.perform(put("/api/v1/requests/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testMarkRequestAsResolved() throws Exception {
        mockMvc.perform(put("/api/v1/requests/{id}/resolve", request.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestStatus.name").value(resolvedStatus.getName()))
                .andExpect(jsonPath("$.resolvedAt").isNotEmpty());
    }

    @Test
    void testMarkRequestAsResolvedNonExistent() throws Exception {
        mockMvc.perform(put("/api/v1/requests/{id}/resolve", 999L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}