package com.proyectapi.tech_suport.request;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDTO {

    private Long id;
    private String description;
    private RequestStatusDTO requestStatus; // objeto anidado
    private Long employeeId;
    private Long requestStatusId;
    private String topic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;

    // Convierte este DTO a entidad
    public RequestEntity toEntity(RequestStatusEntity status) {
    RequestEntity entity = new RequestEntity();
    entity.setId(this.id);
    entity.setDescription(this.description);
    entity.setTopic(this.topic);
    entity.setRequestStatus(status); // ya validado antes
    return entity;
}

    // Convierte una entidad a DTO
    public static RequestDTO fromEntity(RequestEntity entity) {
        if (entity == null) return null;

        RequestStatusDTO statusDTO = null;
        if (entity.getRequestStatus() != null) {
            statusDTO = RequestStatusDTO.builder()
                    .id(entity.getRequestStatus().getId())
                    .name(entity.getRequestStatus().getName())
                    .build();
        }

        return RequestDTO.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .requestStatus(statusDTO)
                .requestStatusId(entity.getRequestStatus() != null ? entity.getRequestStatus().getId() : null)
                .requestStatus(statusDTO)
                .requestStatusId(entity.getRequestStatus() != null ? entity.getRequestStatus().getId() : null)
                .employeeId(entity.getEmployee() != null ? entity.getEmployee().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .resolvedAt(entity.getResolvedAt())
                .build();
    }

    @Data
    @Builder
    public static class RequestStatusDTO {
        private Long id;
        private String name;
    }
}