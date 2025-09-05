package com.proyectapi.tech_suport.employee;

import com.proyectapi.tech_suport.request.RequestEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String department;

    // Relación con RequestEntity
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<RequestEntity> requests;
}