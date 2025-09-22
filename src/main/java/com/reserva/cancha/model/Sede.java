
package com.reserva.cancha.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "sede")
@Data
// Evita que Jackson intente serializar los proxys de Hibernate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
}
