package com.proyectapi.tech_suport.requeststatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "request_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
}