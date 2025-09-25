package com.reserva.cancha.controller;

import com.reserva.cancha.dto.SlotDTO;
import com.reserva.cancha.service.DisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    /**
     * GET /api/disponibilidad?canchaId=1&from=2025-09-30T05:00:00&to=2025-09-30T23:00:00&slotMin=60
     */
    @GetMapping("/disponibilidad")
    public List<SlotDTO> get(
            @RequestParam Long canchaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "60") int slotMin) {
        return disponibilidadService.calcular(canchaId, from, to, slotMin);
    }
}

