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

    @Test
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
    }
}