package com.proyectapi.tech_suport.service;

import java.util.List;

import com.proyectapi.tech_suport.request.RequestEntity;

public interface RequestService {
    RequestEntity createRequest(RequestEntity request); // <-- este método debe existir
    List<RequestEntity> getAllRequests();
    RequestEntity updateRequest(Long id, RequestEntity updatedRequest);
    RequestEntity markAsResolved(Long id, String technicianName);
    void deleteRequest(Long id);
}