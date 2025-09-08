package com.proyectapi.tech_suport.service;

import java.util.List;
import java.util.Optional;
import com.proyectapi.tech_suport.request.RequestStatusEntity;

import com.proyectapi.tech_suport.request.RequestEntity;

public interface RequestService {
    List<RequestEntity> getAllRequests();
    Optional<RequestStatusEntity> findStatusById(Long id);
    RequestEntity createRequest(RequestEntity entity);
    RequestEntity updateRequest(Long id, RequestEntity entity);
    RequestEntity markAsResolved(Long id, String resolvedBy);
    void deleteRequest(Long id);
    List<RequestEntity> getRequestsByEmployee(Long employeeId);
}