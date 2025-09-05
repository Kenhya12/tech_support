package com.proyectapi.tech_suport.request;

import com.proyectapi.tech_suport.employee.EmployeeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity             // Indica que esta clase es una entidad JPA
@Table(name = "request")
@Getter             // Lombok: genera getters automáticamente
@Setter             // Lombok: genera setters automáticamente
@NoArgsConstructor  // Lombok: genera un constructor sin argumentos
@AllArgsConstructor // Lombok: genera un constructor con todos los argumentos
@Builder            // Lombok: genera el patrón de diseño Builder
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private RequestStatusEntity requestStatus;

}