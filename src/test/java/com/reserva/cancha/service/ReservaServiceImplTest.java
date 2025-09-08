package com.reserva.cancha.service;

import com.reserva.cancha.model.Reserva;
import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.repository.ReservaRepository;
import com.reserva.cancha.repository.CanchaRepository;
import com.reserva.cancha.service.impl.ReservaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock private ReservaRepository reservaRepository;
    @Mock private CanchaRepository canchaRepository;

    @InjectMocks private ReservaServiceImpl reservaService;

    @Test
    void crear_ok_cuandoNoHaySolape() {
        // DTO: usa el constructor/record de tu proyecto (ajusta si usas builder)
        var dto = new com.reserva.cancha.dto.ReservaDTO(
                1L,
                "juan",
                Instant.parse("2025-09-07T10:00:00Z"),
                Instant.parse("2025-09-07T11:00:00Z")
        );

        // Mock: la cancha existe
        Cancha cancha = new Cancha();
        cancha.setId(1L);
        when(canchaRepository.findById(1L)).thenReturn(Optional.of(cancha));

        // Mock: NO hay solapes
        when(reservaRepository.solapes(any(), any(), any())).thenReturn(Collections.emptyList());

        // Mock: save devuelve la entidad que le pasan (con id si tu service la setea)
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(a -> a.getArgument(0));

        // No debe lanzar excepción (independiente de si retorna Long/Reserva/void)
        assertDoesNotThrow(() -> reservaService.crear(dto));
    }
}


