package com.proyectapi.tech_suport.request;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity             // Indica que esta clase es una entidad JPA
@Table(name = "request_status") // Nombre de la tabla en la base de datos
@Data               // Lombok: genera getters, setters, toString, equals y hashCode automáticamente
@Builder            // Lombok: genera el patrón de diseño Builder
@NoArgsConstructor  // Lombok: genera un constructor sin argumentos
@AllArgsConstructor // Lombok: genera un constructor con todos los argumentos
public class RequestStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del estado de la solicitud (ej. PENDING, RESOLVED)
    @Column(nullable = false, unique = true)
    private String name;

    // Descripción opcional del estado
    @Column(nullable = true)
    private String status;

    // Fecha y hora en que se creó el estado
    @Column(nullable = false)
    private LocalDateTime createdAt;
}