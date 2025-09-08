package com.proyectapi.tech_suport.repository;

import com.proyectapi.tech_suport.employee.EmployeeEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testSaveAndFindById() {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setName("Juan Pérez");
        employee.setEmail("juan.perez@test.com");

        EmployeeEntity saved = employeeRepository.save(employee);
        Optional<EmployeeEntity> found = employeeRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Juan Pérez");
        assertThat(found.get().getEmail()).isEqualTo("juan.perez@test.com");
    }

    @Test
    void testFindAll() {
        EmployeeEntity emp1 = new EmployeeEntity();
        emp1.setName("Ana Gómez");
        emp1.setEmail("ana@test.com");

        EmployeeEntity emp2 = new EmployeeEntity();
        emp2.setName("Luis Martínez");
        emp2.setEmail("luis@test.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);

        List<EmployeeEntity> allEmployees = employeeRepository.findAll();
        assertThat(allEmployees).hasSizeGreaterThanOrEqualTo(2)
                .extracting(EmployeeEntity::getName)
                .contains("Ana Gómez", "Luis Martínez");
    }

    @Test
    void testDelete() {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setName("Carlos Díaz");
        employee.setEmail("carlos@test.com");

        EmployeeEntity saved = employeeRepository.save(employee);
        Long id = saved.getId();

        employeeRepository.deleteById(id);

        Optional<EmployeeEntity> deleted = employeeRepository.findById(id);
        assertThat(deleted).isNotPresent();
    }

    @Test
    void testUpdateEmployee() {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setName("Laura Sánchez");
        employee.setEmail("laura@test.com");

        EmployeeEntity saved = employeeRepository.save(employee);
        saved.setName("Laura S.");
        saved.setEmail("laura.s@test.com");

        EmployeeEntity updated = employeeRepository.save(saved);
        Optional<EmployeeEntity> found = employeeRepository.findById(updated.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Laura S.");
        assertThat(found.get().getEmail()).isEqualTo("laura.s@test.com");
    }

    @Test
    void testFindByNonExistentId() {
        Optional<EmployeeEntity> found = employeeRepository.findById(-1L);
        assertThat(found).isNotPresent();
    }

    @Test
    void testDeleteNonExistentEmployee() {
        // Deleting a non-existent employee should not throw exception
        employeeRepository.deleteById(-1L);

        // Verify repository still works
        EmployeeEntity employee = new EmployeeEntity();
        employee.setName("Test User");
        employee.setEmail("test.user@test.com");

        EmployeeEntity saved = employeeRepository.save(employee);
        assertThat(saved.getId()).isNotNull();
    }
}
