package com.proyectapi.tech_suport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyectapi.tech_suport.employee.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    // Puedes agregar consultas personalizadas si lo necesitas más adelante
}