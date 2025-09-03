
package com.reserva.cancha.service;

import com.reserva.cancha.dto.ReservaDTO;
import com.reserva.cancha.model.Reserva;

import java.time.Instant;
import java.util.List;

public interface ReservaService {
    Reserva crear(ReservaDTO dto);
    void cancelar(Long id);
    List<Reserva> ocupadas(Long canchaId, Instant desde, Instant hasta);
}
