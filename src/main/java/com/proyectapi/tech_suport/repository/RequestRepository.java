package com.proyectapi.tech_suport.repository;

import com.proyectapi.tech_suport.request.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    List<RequestEntity> findAllByOrderByCreatedAtAsc(); 
    
}


