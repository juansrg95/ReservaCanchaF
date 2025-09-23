
package com.reserva.cancha.service;

import com.reserva.cancha.dto.ReservaRequest;
import com.reserva.cancha.dto.ReservaResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaService {
    List<ReservaResponse> listar(Optional<Long> canchaId, Optional<LocalDateTime> desde, Optional<LocalDateTime> hasta);

    ReservaResponse crear(ReservaRequest request);

    void eliminar(Long id, String solicitanteUsername, boolean solicitanteEsAdmin);
}

