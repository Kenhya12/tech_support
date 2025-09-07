package com.proyectapi.tech_suport.controller;

import com.proyectapi.tech_suport.request.RequestDTO;
import com.proyectapi.tech_suport.request.RequestDTO.RequestStatusDTO;
import com.proyectapi.tech_suport.request.RequestEntity;
import com.proyectapi.tech_suport.request.RequestStatusEntity;
import com.proyectapi.tech_suport.service.RequestService;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDTO> getAllRequests() {
        return requestService.getAllRequests()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
public RequestDTO updateRequest(@PathVariable Long id, @RequestBody RequestDTO dto) {
    try {
        RequestStatusEntity status = null;
        if (dto.getRequestStatusId() != null) {
            status = requestService.findStatusById(dto.getRequestStatusId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status ID"));
        }
        RequestEntity entity = dto.toEntity(status);
        RequestEntity updated = requestService.updateRequest(id, entity);
        return toDTO(updated);
    } catch (RuntimeException ex) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}

    @PutMapping("/{id}/resolve")
    public RequestDTO markAsResolved(@PathVariable Long id) {
        try {
            RequestEntity resolved = requestService.markAsResolved(id, null);
            return toDTO(resolved);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping
public RequestDTO createRequest(@RequestBody RequestDTO dto) {
    try {
        RequestStatusEntity status = null;
        if (dto.getRequestStatusId() != null) {
            status = requestService.findStatusById(dto.getRequestStatusId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status ID"));
        }
        RequestEntity entity = dto.toEntity(status);
        RequestEntity saved = requestService.createRequest(entity);
        return toDTO(saved);
    } catch (RuntimeException ex) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}

    private RequestDTO toDTO(RequestEntity entity) {
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
                .employeeId(entity.getEmployee() != null ? entity.getEmployee().getId() : null)
                .topic(entity.getTopic())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .resolvedAt(entity.getResolvedAt())
                .build();
    }
}