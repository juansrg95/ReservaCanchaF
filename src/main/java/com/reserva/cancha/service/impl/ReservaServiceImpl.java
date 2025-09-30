
package com.reserva.cancha.service.impl;

import com.reserva.cancha.dto.ReservaRequest;
import com.reserva.cancha.dto.ReservaResponse;
import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.model.Reserva;
import com.reserva.cancha.repository.CanchaRepository;
import com.reserva.cancha.repository.ReservaRepository;
import com.reserva.cancha.repository.UsuarioRepository;
import com.reserva.cancha.service.ReservaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final CanchaRepository canchaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              CanchaRepository canchaRepository,
                              UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.canchaRepository = canchaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listar(Optional<Long> canchaId,
                                        Optional<LocalDateTime> desde,
                                        Optional<LocalDateTime> hasta) {
        List<Reserva> reservas;

        if (canchaId.isPresent() && desde.isPresent() && hasta.isPresent()) {
            reservas = reservaRepository.findByCancha_IdAndInicioBetweenOrderByInicioAsc(
                    canchaId.get(), desde.get(), hasta.get());
        } else if (canchaId.isPresent()) {
            reservas = reservaRepository.findByCancha_IdOrderByInicioAsc(canchaId.get());
        } else if (desde.isPresent() && hasta.isPresent()) {
            reservas = reservaRepository.findByInicioBetweenOrderByInicioAsc(desde.get(), hasta.get());
        } else {
            reservas = reservaRepository.findAll().stream()
                    .sorted(Comparator.comparing(Reserva::getInicio))
                    .toList();
        }

        return reservas.stream().map(this::toResponse).toList();
    }

    @Override
    public ReservaResponse crear(ReservaRequest request) {
        // Validaciones básicas de tiempo
        if (request.getInicio() == null || request.getFin() == null) {
            throw new IllegalArgumentException("Debe enviar 'inicio' y 'fin'.");
        }
        if (!request.getFin().isAfter(request.getInicio())) {
            throw new IllegalArgumentException("'fin' debe ser posterior a 'inicio'.");
        }

        // Cancha
        Cancha cancha = canchaRepository.findById(request.getCanchaId())
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada"));

        // Usuario (por username, tal como está el esquema)
        boolean existeUsuario = usuarioRepository.findByUsername(request.getUsuario()).isPresent();
        if (!existeUsuario) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        // Chequeo de solapamiento
        boolean hayChoque = reservaRepository.existeChoque(
                request.getCanchaId(),
                request.getInicio(),
                request.getFin()
        );
        if (hayChoque) {
            throw new IllegalArgumentException(
                    "Conflicto de horario: ya existe una reserva para esa cancha en ese rango.");
        }

        // Persistir
        Reserva r = new Reserva();
        r.setCancha(cancha);
        r.setUsuario(request.getUsuario());   // en BD es texto (username)
        r.setInicio(request.getInicio());
        r.setFin(request.getFin());
        r.setEstado("ACTIVA");

        Reserva guardada = reservaRepository.save(r);
        return toResponse(guardada);
    }

    @Override
    public void eliminar(Long id, String solicitanteUsername, boolean solicitanteEsAdmin) {
        Reserva r = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

        // El admin puede borrar cualquiera; el user solo las suyas
        if (!solicitanteEsAdmin && !r.getUsuario().equalsIgnoreCase(solicitanteUsername)) {
            throw new IllegalArgumentException("No tienes permiso para eliminar esta reserva.");
        }
        reservaRepository.deleteById(id);
    }

    private ReservaResponse toResponse(Reserva r) {
        return new ReservaResponse(
                r.getId(),
                r.getCancha().getId(),
                r.getCancha().getNombre(),
                r.getUsuario(),
                r.getInicio(),
                r.getFin(),
                r.getEstado()
        );
    }
}



