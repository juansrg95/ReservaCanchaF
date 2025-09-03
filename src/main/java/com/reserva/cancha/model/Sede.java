
package com.reserva.cancha.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sede")
@Data
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String direccion;
}
