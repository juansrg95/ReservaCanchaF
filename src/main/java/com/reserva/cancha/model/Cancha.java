
package com.reserva.cancha.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cancha")
@Data
// Por si Hibernate devuelve proxys también para esta entidad
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mantén LAZY si quieres; con la anotación en Sede ya no rompe la serialización
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sede_id")
    private Sede sede;

    private String nombre;
    private String deporte;
    private Boolean activa = true;
}

