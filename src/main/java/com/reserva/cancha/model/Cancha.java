
package com.reserva.cancha.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cancha")
@Data
public class Cancha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sede_id")
    private Sede sede;

    private String nombre;
    private String deporte;
    private Boolean activa = true;
}
