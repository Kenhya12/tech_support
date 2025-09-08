package com.proyectapi.tech_suport.controller;

import com.proyectapi.tech_suport.request.RequestStatusEntity;
import com.proyectapi.tech_suport.service.RequestStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/request-status")
public class RequestStatusController {

    private final RequestStatusService requestStatusService;

    public RequestStatusController(RequestStatusService requestStatusService) {
        this.requestStatusService = requestStatusService;
    }

    // GET /api/v1/request-status
    @GetMapping
    public List<RequestStatusEntity> getAllStatuses() {
        return requestStatusService.getAllStatuses();
    }

    // GET /api/v1/request-status/{id}
    @GetMapping("/{id}")
    public RequestStatusEntity getStatusById(@PathVariable Long id) {
        return requestStatusService.getStatusById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found"));
    }

    // POST /api/v1/request-status
    @PostMapping
    public RequestStatusEntity createStatus(@RequestBody RequestStatusEntity status) {
        try {
            return requestStatusService.createStatus(status);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    // PUT /api/v1/request-status/{id}
    @PutMapping("/{id}")
    public RequestStatusEntity updateStatus(@PathVariable Long id, @RequestBody RequestStatusEntity status) {
        try {
            return requestStatusService.updateStatus(id, status);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    // DELETE /api/v1/request-status/{id}
    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Long id) {
        try {
            requestStatusService.deleteStatus(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
