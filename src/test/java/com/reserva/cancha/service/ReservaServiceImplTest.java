package com.reserva.cancha.service;

import com.reserva.cancha.dto.ReservaRequest;
import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.model.Reserva;
import com.reserva.cancha.model.Usuario;
import com.reserva.cancha.repository.CanchaRepository;
import com.reserva.cancha.repository.ReservaOverlapRepository;
import com.reserva.cancha.repository.ReservaRepository;
import com.reserva.cancha.repository.UsuarioRepository;
import com.reserva.cancha.service.impl.ReservaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock private ReservaRepository reservaRepository;
    @Mock private CanchaRepository canchaRepository;
    @Mock private ReservaOverlapRepository reservaOverlapRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private ReservaServiceImpl reservaService;

    private LocalDateTime t0, t1;
    private Cancha cancha;

    @BeforeEach
    void setup() {
        t0 = LocalDateTime.of(2025, 9, 7, 10, 0);
        t1 = LocalDateTime.of(2025, 9, 7, 11, 0);

        cancha = new Cancha();
        cancha.setId(1L);
        cancha.setNombre("Cancha 1");

        // Cancha existe
        when(canchaRepository.findById(1L)).thenReturn(Optional.of(cancha));

        // Usuario válido (tu servicio usa findByUsername(...).isPresent())
        Usuario u = new Usuario();
        u.setUsername("juan");
        when(usuarioRepository.findByUsername("juan")).thenReturn(Optional.of(u));

        // save lenient: este stub no se usa en el test de solape, evita UnnecessaryStubbing
        lenient().when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> {
            Reserva r = inv.getArgument(0);
            if (r.getId() == null) r.setId(99L);
            return r;
        });
    }

    @Test
    void crear_ok_cuandoNoHaySolape() {
        when(reservaOverlapRepository.findAllByCancha(1L))
                .thenReturn(Collections.emptyList());

        ReservaRequest req = new ReservaRequest();
        req.setCanchaId(1L);
        req.setUsuario("juan");
        req.setInicio(t0);
        req.setFin(t1);

        assertDoesNotThrow(() -> reservaService.crear(req));
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void crear_falla_cuandoHaySolape() {
        // Reserva existente que SOLAPA con [t0, t1]
        Reserva existente = new Reserva();
        existente.setId(10L);
        existente.setCancha(cancha);
        existente.setInicio(LocalDateTime.of(2025, 9, 7, 10, 30));
        existente.setFin(LocalDateTime.of(2025, 9, 7, 11, 30));

        when(reservaOverlapRepository.findAllByCancha(1L))
                .thenReturn(List.of(existente));

        ReservaRequest req = new ReservaRequest();
        req.setCanchaId(1L);
        req.setUsuario("juan");
        req.setInicio(t0);
        req.setFin(t1);

        // Solo verificamos el tipo de excepción (tu mensaje puede ser "Choque..." u otro)
        assertThrows(IllegalArgumentException.class, () -> reservaService.crear(req));

        // Nunca debe intentar guardar
        verify(reservaRepository, never()).save(any(Reserva.class));
    }
}







