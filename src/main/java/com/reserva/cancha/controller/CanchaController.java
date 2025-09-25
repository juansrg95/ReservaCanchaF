package com.reserva.cancha.controller;

import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.service.CanchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
public class CanchaController {

    private final CanchaService canchaService;

    // GET público usado por el frontend
    @GetMapping("/activas")
    public List<Cancha> activas() {
        return canchaService.listarActivas();
    }

    // CRUD ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Cancha> crear(@RequestBody Cancha body) {
        Cancha created = canchaService.crear(body);
        return ResponseEntity.created(URI.create("/api/canchas/" + created.getId())).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Cancha actualizar(@PathVariable Long id, @RequestBody Cancha body) {
        return canchaService.actualizar(id, body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        canchaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}


