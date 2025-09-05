package com.proyectapi.tech_suport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectapi.tech_suport.employee.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    // Aquí puedes agregar métodos personalizados si los necesitas
}
