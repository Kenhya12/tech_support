package com.proyectapi.tech_suport.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.proyectapi.tech_suport.implementations.RequestServiceImpl;
import com.proyectapi.tech_suport.repository.RequestRepository;
import com.proyectapi.tech_suport.repository.RequestStatusRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestStatusRepository requestStatusRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    private RequestEntity request;
    private RequestStatusEntity pendingStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pendingStatus = new RequestStatusEntity();
        pendingStatus.setName("PENDING");

        request = new RequestEntity();
        request.setDescription("Test request");
    }

    @Test
    void testCreateRequest() {
        // Simular que requestStatusRepository devuelve el estado PENDING
        when(requestStatusRepository.findByName("PENDING")).thenReturn(Optional.of(pendingStatus));

        // Simular que requestRepository guarda el request y mantiene el RequestStatus
        when(requestRepository.save(any(RequestEntity.class))).thenAnswer(invocation -> {
            RequestEntity r = invocation.getArgument(0);
            r.setRequestStatus(pendingStatus); // importante: asignamos el estado
            return r;
        });

        RequestEntity created = requestService.createRequest(request);

        assertNotNull(created);
        assertEquals("Test request", created.getDescription());
        assertNotNull(created.getRequestStatus());
        assertEquals("PENDING", created.getRequestStatus().getName());

        verify(requestStatusRepository, times(1)).findByName("PENDING");
        verify(requestRepository, times(1)).save(request);
    }

    @Test
    void testGetAllRequests() {
        RequestEntity req1 = new RequestEntity();
        req1.setDescription("Request 1");
        req1.setRequestStatus(pendingStatus);

        RequestEntity req2 = new RequestEntity();
        req2.setDescription("Request 2");
        req2.setRequestStatus(pendingStatus);

        List<RequestEntity> mockList = Arrays.asList(req1, req2);

        // Simulamos que el repositorio devuelve esta lista
        when(requestRepository.findAllByOrderByCreatedAtAsc()).thenReturn(mockList);

        List<RequestEntity> requests = requestService.getAllRequests();

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals("Request 1", requests.get(0).getDescription());
        assertEquals("Request 2", requests.get(1).getDescription());

        verify(requestRepository, times(1)).findAllByOrderByCreatedAtAsc();
    }

    @Test
    void testUpdateRequest() {
        // Request existente
        RequestEntity existingRequest = new RequestEntity();
        existingRequest.setId(1L);
        existingRequest.setDescription("Request original");
        existingRequest.setRequestStatus(pendingStatus);

        // Datos actualizados
        RequestEntity updatedRequest = new RequestEntity();
        updatedRequest.setDescription("Request actualizado");

        // Mock: findById devuelve el request existente
        when(requestRepository.findById(1L)).thenReturn(Optional.of(existingRequest));
        // Mock: save devuelve el mismo objeto con los cambios aplicados
        when(requestRepository.save(any(RequestEntity.class))).thenAnswer(invocation -> {
            RequestEntity r = invocation.getArgument(0);
            r.setRequestStatus(pendingStatus); // aseguramos que tenga el estado
            return r;
        });
        // Llamada al servicio
        RequestEntity result = requestService.updateRequest(1L, updatedRequest);

        // Assertions
        assertNotNull(result);
        assertEquals("Request actualizado", result.getDescription());
        // Verificar que el RequestStatus no se perdió
        assertEquals(pendingStatus, result.getRequestStatus());

        // Verificaciones de mocks
        verify(requestRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).save(existingRequest);
    }

    @Test
    void testMarkAsResolved() {
        // Estado Resolved simulado
        RequestStatusEntity resolvedStatus = new RequestStatusEntity();
        resolvedStatus.setName("RESOLVED");

        // Request existente
        RequestEntity existingRequest = new RequestEntity();
        existingRequest.setId(1L);
        existingRequest.setDescription("Request pendiente");
        existingRequest.setRequestStatus(pendingStatus);

        // Mock: repositorios
        when(requestRepository.findById(1L)).thenReturn(Optional.of(existingRequest));
        when(requestStatusRepository.findByName("RESOLVED")).thenReturn(Optional.of(resolvedStatus));
        when(requestRepository.save(any(RequestEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Llamada al servicio
        RequestEntity result = requestService.markAsResolved(1L, "Technician X");

        // Assertions
        assertNotNull(result.getResolvedAt(), "El campo resolvedAt debe haberse asignado");
        assertEquals("RESOLVED", result.getRequestStatus().getName(), "El estado debe ser Resolved");

        // Verificaciones de mocks
        verify(requestRepository, times(1)).findById(1L);
        verify(requestStatusRepository, times(1)).findByName("RESOLVED");
        verify(requestRepository, times(1)).save(existingRequest);
    }

    @Test
    void testUpdateRequest_NotFound() {
        // Simulamos que no se encuentra el request
        when(requestRepository.findById(99L)).thenReturn(Optional.empty());

        // Ejecutamos y verificamos que lanza excepción
        long id = 99L;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            RequestEntity updatedRequest = new RequestEntity();
            updatedRequest.setDescription("Request inexistente");
            requestService.updateRequest(id, updatedRequest);
        });

        assertEquals("Request not found with id " + id, exception.getMessage());
        verify(requestRepository, times(1)).findById(99L);
        verify(requestRepository, never()).save(any(RequestEntity.class));
    }

    @Test
    void testDeleteRequest_Resolved() {
        // Request existente con estado RESOLVED
        RequestStatusEntity resolvedStatus = new RequestStatusEntity();
        resolvedStatus.setName("RESOLVED");

        RequestEntity resolvedRequest = new RequestEntity();
        resolvedRequest.setId(1L);
        resolvedRequest.setDescription("Request resuelta");
        resolvedRequest.setRequestStatus(resolvedStatus);

        // Mock: findById devuelve la solicitud
        when(requestRepository.findById(1L)).thenReturn(Optional.of(resolvedRequest));

        // Ejecutar deleteRequest
        requestService.deleteRequest(1L);

        // Verificar que deleteById fue llamado
        verify(requestRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRequest_NotResolved() {
        // Request existente con estado PENDING
        RequestEntity pendingRequest = new RequestEntity();
        pendingRequest.setId(2L);
        pendingRequest.setDescription("Request pendiente");
        pendingRequest.setRequestStatus(pendingStatus); // PENDING

        when(requestRepository.findById(2L)).thenReturn(Optional.of(pendingRequest));

        // Ejecutar deleteRequest y esperar excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            requestService.deleteRequest(2L);
        });

        assertEquals("Cannot delete a request that is not resolved", exception.getMessage());

        // Verificar que deleteById no fue llamado
        verify(requestRepository, never()).deleteById(anyLong());
    }
}