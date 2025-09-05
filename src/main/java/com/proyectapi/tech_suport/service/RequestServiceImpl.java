package com.proyectapi.tech_suport.service;

import com.proyectapi.tech_suport.request.RequestEntity;
import com.proyectapi.tech_suport.request.RequestStatusEntity;
import com.proyectapi.tech_suport.repository.RequestRepository;
import com.proyectapi.tech_suport.repository.RequestStatusRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestStatusRepository requestStatusRepository;

    public RequestServiceImpl(RequestRepository requestRepository, RequestStatusRepository requestStatusRepository) {    
        this.requestRepository = requestRepository;
        this.requestStatusRepository = requestStatusRepository;
    }

    @Override
    public RequestEntity createRequest(RequestEntity request) {
        // Obtener el estado inicial
        RequestStatusEntity pendingStatus = requestStatusRepository.findByName("PENDING")
                .orElseThrow(() -> new RuntimeException("Initial status not found"));
        request.setRequestStatus(pendingStatus);
        return requestRepository.save(request);
    }

    @Override
    public List<RequestEntity> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtAsc();
    }

    @Override
    public RequestEntity updateRequest(Long id, RequestEntity updatedRequest) {
        return requestRepository.findById(id)
                .map(req -> {
                req.setDescription(updatedRequest.getDescription());
                req.setRequestStatus(updatedRequest.getRequestStatus());
                req.setEmployee(updatedRequest.getEmployee());
                req.setTopic(updatedRequest.getTopic());
                req.setUpdatedAt(java.time.LocalDateTime.now());
                    return requestRepository.save(req);
                }).orElseThrow(() -> new RuntimeException("Request not found with id \" + id"));
    }

    @Override
    public RequestEntity markAsResolved(Long id, String technicianName) {
        return requestRepository.findById(id)
                .map(req -> {
                    RequestStatusEntity resolvedStatus = requestStatusRepository.findByName("RESOLVED")
                            .orElseThrow(() -> new RuntimeException("Resolved status not found"));
                    req.setRequestStatus(resolvedStatus);
                    req.setResolvedAt(java.time.LocalDateTime.now());
                    return requestRepository.save(req);
                }).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    @Override
    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}