package com.proyectapi.tech_suport.implementations;

import com.proyectapi.tech_suport.exceptions.ResourceNotFoundException;
import com.proyectapi.tech_suport.request.RequestStatusEntity;
import com.proyectapi.tech_suport.repository.RequestStatusRepository;
import com.proyectapi.tech_suport.service.RequestStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz RequestStatusService.
 * Proporciona operaciones CRUD para RequestStatusEntity.
 */
@Service
public class RequestStatusServiceImpl implements RequestStatusService {

    private final RequestStatusRepository requestStatusRepository;

    public RequestStatusServiceImpl(RequestStatusRepository requestStatusRepository) {
        this.requestStatusRepository = requestStatusRepository;
    }

    /**
     * Obtiene todos los estados de solicitud existentes.
     *
     * @return Lista de RequestStatusEntity
     */
    @Override
    public List<RequestStatusEntity> getAllStatuses() {
        return requestStatusRepository.findAll();
    }

    /**
     * Obtiene un estado de solicitud por su ID.
     *
     * @param id ID del estado
     * @return Optional con RequestStatusEntity si existe
     */
    @Override
    public Optional<RequestStatusEntity> getStatusById(Long id) {
        return requestStatusRepository.findById(id);
    }

    /**
     * Crea un nuevo estado de solicitud.
     *
     * @param status entidad a crear
     * @return entidad creada
     */
    @Override
    public RequestStatusEntity createStatus(RequestStatusEntity status) {
        return requestStatusRepository.save(status);
    }

    /**
     * Actualiza un estado de solicitud existente por ID.
     * Lanza ResourceNotFoundException si no existe.
     *
     * @param id ID del estado
     * @param status datos para actualizar
     * @return entidad actualizada
     */
    @Override
    public RequestStatusEntity updateStatus(Long id, RequestStatusEntity status) {
        return requestStatusRepository.findById(id)
                .map(existing -> {
                    existing.setName(status.getName());
                    return requestStatusRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        "RequestStatus with id " + id + " not found"));
    }

    /**
     * Elimina un estado de solicitud por ID.
     * Lanza ResourceNotFoundException si no existe.
     *
     * @param id ID del estado
     */
    @Override
    public void deleteStatus(Long id) {
        if (!requestStatusRepository.existsById(id)) {
            throw new ResourceNotFoundException("RequestStatus with id " + id + " not found");
        }
        requestStatusRepository.deleteById(id);
    }
}