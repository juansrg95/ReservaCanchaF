package com.reserva.cancha.model;

import jakarta.persistence.*;           //  Importa todas las anotaciones JPA
import lombok.*;                        //  Importa todas las anotaciones Lombok
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cancha_id", nullable = false)
    private Cancha cancha;

    @Column(name = "usuario", nullable = false, length = 255)
    private String usuario;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fin", nullable = false)
    private LocalDateTime fin;

    @Column(name = "estado", nullable = false, length = 255)
    private String estado;
}


