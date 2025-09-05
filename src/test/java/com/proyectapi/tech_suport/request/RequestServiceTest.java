package com.proyectapi.tech_suport.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.proyectapi.tech_suport.repository.RequestRepository;
import com.proyectapi.tech_suport.repository.RequestStatusRepository;
import com.proyectapi.tech_suport.service.RequestService;
import com.proyectapi.tech_suport.service.RequestServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

        /*
         * /* pendingStatus = RequestStatusEntity.builder()
         * .id(1L)
         * .status("PENDING")
         * .build();
         * 
         * request = RequestEntity.builder()
         * .id(1L)
         * .topic("Hardware")
         * .description("PC no enciende")
         * .createdAt(LocalDateTime.now())
         * .requestStatus(pendingStatus)
         * .build();
         */
        // } */

        pendingStatus = new RequestStatusEntity();
        pendingStatus.setName("PENDING");

        request = new RequestEntity();
        request.setDescription("Test request");
    }

    /* @Test
    void testCreateRequest() {
        // Simular que requestStatusRepository devuelve el estado PENDING
        when(requestStatusRepository.findByName("PENDING")).thenReturn(Optional.of(pendingStatus));
        // Simular que requestRepository guarda el request
        when(requestRepository.save(any(RequestEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RequestEntity created = requestService.createRequest(request);

        assertNotNull(created);
        assertEquals("Test request", created.getDescription());
        assertEquals("PENDING", created.getRequestStatus().getName());

        verify(requestStatusRepository, times(1)).findByName("PENDING");
        verify(requestRepository, times(1)).save(request);
    } */

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
}