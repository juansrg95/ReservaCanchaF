
package com.reserva.cancha.controller;

import com.reserva.cancha.dto.ReservaDTO;
import com.reserva.cancha.model.Reserva;
import com.reserva.cancha.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService service;

    @PostMapping
    public ResponseEntity<Reserva> crear(@RequestBody ReservaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ocupadas")
    public List<Reserva> ocupadas(@RequestParam Long canchaId,
                                  @RequestParam Instant desde,
                                  @RequestParam Instant hasta) {
        return service.ocupadas(canchaId, desde, hasta);
    }
}
