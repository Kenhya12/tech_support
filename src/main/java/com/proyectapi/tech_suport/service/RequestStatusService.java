package com.proyectapi.tech_suport.service;

import com.proyectapi.tech_suport.request.RequestStatusEntity;

import java.util.List;
import java.util.Optional;

public interface RequestStatusService {
    List<RequestStatusEntity> getAllStatuses();
    Optional<RequestStatusEntity> getStatusById(Long id);
    RequestStatusEntity createStatus(RequestStatusEntity status);
    RequestStatusEntity updateStatus(Long id, RequestStatusEntity status);
    void deleteStatus(Long id);
}
