package com.reserva.cancha.service;

import com.reserva.cancha.dto.SlotDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface DisponibilidadService {
    List<SlotDTO> calcular(Long canchaId, LocalDateTime from, LocalDateTime to, int slotMin);
}
