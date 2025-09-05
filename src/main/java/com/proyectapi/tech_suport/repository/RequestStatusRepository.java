package com.proyectapi.tech_suport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyectapi.tech_suport.request.RequestStatusEntity;

import java.util.Optional;

@Repository
public interface RequestStatusRepository extends JpaRepository<RequestStatusEntity, Long> {
    Optional<RequestStatusEntity> findByName(String name);
    Optional<RequestStatusEntity> findByStatus(String status);
}



