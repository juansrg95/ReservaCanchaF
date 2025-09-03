
package com.reserva.cancha.service.impl;

import com.reserva.cancha.dto.ReservaDTO;
import com.reserva.cancha.model.Reserva;
import com.reserva.cancha.repository.CanchaRepository;
import com.reserva.cancha.repository.ReservaRepository;
import com.reserva.cancha.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepo;
    private final CanchaRepository canchaRepo;

    @Override
    public Reserva crear(ReservaDTO dto) {
        var cancha = canchaRepo.findById(dto.canchaId()).orElseThrow();
        if (!reservaRepo.solapes(dto.canchaId(), dto.inicio(), dto.fin()).isEmpty()) {
            throw new IllegalStateException("La cancha no está disponible en ese horario");
        }
        var r = new Reserva();
        r.setCancha(cancha);
        r.setUsuario(dto.usuario());
        r.setInicio(dto.inicio());
        r.setFin(dto.fin());
        r.setEstado("ACTIVA");
        return reservaRepo.save(r);
    }

    @Override
    public void cancelar(Long id) {
        var r = reservaRepo.findById(id).orElseThrow();
        r.setEstado("CANCELADA");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> ocupadas(Long canchaId, Instant desde, Instant hasta) {
        return reservaRepo.solapes(canchaId, desde, hasta);
    }
}
