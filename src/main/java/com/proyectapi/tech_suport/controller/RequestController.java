package com.proyectapi.tech_suport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyectapi.tech_suport.request.RequestEntity;
import com.proyectapi.tech_suport.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    // Crear nueva solicitud
    @PostMapping
    public ResponseEntity<RequestEntity> createRequest(@RequestBody RequestEntity request) {
        RequestEntity savedRequest = requestService.createRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    // Obtener todas las solicitudes
    @GetMapping
    public ResponseEntity<List<RequestEntity>> getAllRequests() {
        List<RequestEntity> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    // Actualizar solicitud
    @PutMapping("/{id}")
    public ResponseEntity<RequestEntity> updateRequest(
            @PathVariable Long id,
            @RequestBody RequestEntity updatedRequest) {
        RequestEntity request = requestService.updateRequest(id, updatedRequest);
        return ResponseEntity.ok(request);
    }

    // Marcar como resuelta
    @PutMapping("/{id}/resolve")
    public ResponseEntity<RequestEntity> markAsResolved(
            @PathVariable Long id,
            @RequestParam String technicianName) {
        RequestEntity request = requestService.markAsResolved(id, technicianName);
        return ResponseEntity.ok(request);
    }

    // Eliminar solicitud (solo si está resuelta)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
