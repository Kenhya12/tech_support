package com.proyectapi.tech_suport.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class RequestDTOTest {

    @Test
    void testDTOToEntityAndBack() {
        // Creamos un RequestStatusEntity
        RequestStatusEntity status = new RequestStatusEntity();
        status.setId(1L);
        status.setName("PENDING");

        // Creamos el DTO con datos de ejemplo
        RequestDTO dto = RequestDTO.builder()
                .id(10L)
                .description("Test request")
                .topic("Test topic")
                .requestStatus(RequestDTO.RequestStatusDTO.builder()
                        .id(1L)
                        .name("PENDING")
                        .build())
                .employeeId(5L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .resolvedAt(null)
                .build();

        // Conversión a Entity
        RequestEntity entity = dto.toEntity(status);
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getTopic(), entity.getTopic());
        assertEquals(status, entity.getRequestStatus());

        // Conversión de Entity a DTO
        RequestDTO converted = RequestDTO.fromEntity(entity);
        assertEquals(entity.getDescription(), converted.getDescription());
        assertEquals(entity.getTopic(), converted.getTopic());
        assertEquals(entity.getRequestStatus().getName(), converted.getRequestStatus().getName());
        assertEquals(entity.getRequestStatus().getId(), converted.getRequestStatusId());
        assertNotNull(converted.getRequestStatus());
        assertEquals(entity.getRequestStatus().getName(), converted.getRequestStatus().getName());
    }

    @Test
    void testNullEntityConversion() {
        RequestDTO converted = RequestDTO.fromEntity(null);
        assertNull(converted);
    }

    @Test
    void testNullStatusInEntity() {
        RequestEntity entity = new RequestEntity();
        entity.setId(1L);
        entity.setDescription("No status request");
        entity.setTopic("Topic X");

        RequestDTO dto = RequestDTO.fromEntity(entity);
        assertNull(dto.getRequestStatus());
        assertNull(dto.getRequestStatusName());
        assertNull(dto.getRequestStatusId());
    }
}