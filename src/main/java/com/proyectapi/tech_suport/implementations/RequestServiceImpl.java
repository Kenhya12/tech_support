package com.proyectapi.tech_suport.implementations;

import com.proyectapi.tech_suport.request.RequestEntity;
import com.proyectapi.tech_suport.request.RequestStatusEntity;
import com.proyectapi.tech_suport.service.RequestService;

import jakarta.annotation.PostConstruct;


import com.proyectapi.tech_suport.repository.RequestRepository;
import com.proyectapi.tech_suport.repository.RequestStatusRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestStatusRepository requestStatusRepository;

    public RequestServiceImpl(RequestRepository requestRepository, RequestStatusRepository requestStatusRepository) {
        this.requestRepository = requestRepository;
        this.requestStatusRepository = requestStatusRepository;
    }

    @PostConstruct
    public void initStatuses() {
        if (requestStatusRepository.count() == 0) {
            LocalDateTime now = LocalDateTime.now();

            requestStatusRepository.save(
                    RequestStatusEntity.builder()
                            .name("PENDING")
                            .createdAt(now)
                            .build());

            requestStatusRepository.save(
                    RequestStatusEntity.builder()
                            .name("RESOLVED")
                            .createdAt(now)
                            .build());
        }
    }

    @Override
    public RequestEntity createRequest(RequestEntity request) {
        // Obtiene el estado inicial "PENDING" desde la base de datos
        RequestStatusEntity pendingStatus = requestStatusRepository
                .findByName("PENDING")
                .orElseThrow(() -> new RuntimeException("Initial status not found"));

        // Asigna el estado y la fecha de creación
        request.setRequestStatus(pendingStatus);
        request.setCreatedAt(LocalDateTime.now());

        // Guarda la solicitud y retorna la entidad persistida
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
                    req.setUpdatedAt(LocalDateTime.now());
                    return requestRepository.save(req);
                }).orElseThrow(() -> new RuntimeException("Request not found with id " + id));
    }

    @Override
    public RequestEntity markAsResolved(Long id, String technicianName) {
        return requestRepository.findById(id)
                .map(req -> {
                    RequestStatusEntity resolvedStatus = requestStatusRepository.findByName("RESOLVED")
                            .orElseThrow(() -> new RuntimeException("Resolved status not found"));
                    req.setRequestStatus(resolvedStatus);
                    req.setResolvedAt(LocalDateTime.now());
                    return requestRepository.save(req);
                }).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    @Override
    public void deleteRequest(Long id) {
        requestRepository.findById(id).ifPresent(request -> {
            if ("RESOLVED".equals(request.getRequestStatus().getName())) {
                requestRepository.deleteById(id);
            } else {
                throw new RuntimeException("Cannot delete a request that is not resolved");
            }
        });
    }

    @Override
    public Optional<RequestStatusEntity> findStatusById(Long id) {
        return requestStatusRepository.findById(id);
    }
}