package com.reserva.cancha.service;

import com.reserva.cancha.dto.ReservaRequest;
import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.model.Reserva;
import com.reserva.cancha.model.Usuario;
import com.reserva.cancha.repository.CanchaRepository;
import com.reserva.cancha.repository.ReservaRepository;
import com.reserva.cancha.repository.UsuarioRepository;
import com.reserva.cancha.service.impl.ReservaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock private ReservaRepository reservaRepository;
    @Mock private CanchaRepository canchaRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private ReservaServiceImpl reservaService;

    private LocalDateTime t0, t1;

    // Método para asignar valores a campos privados sin usar setters
    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        t0 = LocalDateTime.of(2025, 9, 7, 10, 0); // 10:00
        t1 = LocalDateTime.of(2025, 9, 7, 11, 0); // 11:00

        // Crear cancha sin setters (usando reflexión)
        Cancha cancha = new Cancha();
        setField(cancha, "id", 1L);
        setField(cancha, "nombre", "Cancha 1");
        when(canchaRepository.findById(1L)).thenReturn(Optional.of(cancha));

        // Crear usuario sin setters (usando reflexión)
        Usuario u = new Usuario();
        setField(u, "username", "juan");
        when(usuarioRepository.findByUsername("juan")).thenReturn(Optional.of(u));

        // Guardado lenient para evitar errores de Mockito
        lenient().when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crear_ok_cuandoNoHaySolape() {
        when(reservaRepository.existeChoque(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);

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
        when(reservaRepository.existeChoque(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(true);

        ReservaRequest req = new ReservaRequest();
        req.setCanchaId(1L);
        req.setUsuario("juan");
        req.setInicio(t0);
        req.setFin(t1);

        assertThrows(IllegalArgumentException.class, () -> reservaService.crear(req));
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void crear_pasa_cuandoEsContigua_noHaySolape() {
        // 11:00–12:00 contiguo a 10:00–11:00 → NO debe considerarse solape
        when(reservaRepository.existeChoque(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);

        ReservaRequest req = new ReservaRequest();
        req.setCanchaId(1L);
        req.setUsuario("juan");
        req.setInicio(t1);            // 11:00
        req.setFin(t1.plusHours(1));  // 12:00

        assertDoesNotThrow(() -> reservaService.crear(req));
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }
}










