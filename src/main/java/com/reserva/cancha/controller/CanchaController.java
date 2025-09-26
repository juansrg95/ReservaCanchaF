package com.reserva.cancha.controller;

import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.service.CanchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // ====== GET PÚBLICOS (para el frontend) ======

    // El frontend llama a /api/canchas -> aquí devolvemos las activas
    @GetMapping
    public List<Cancha> listar() {
        return canchaService.listarActivas();
    }

    // (Opcional: por si algún cliente pega barra final)
    @GetMapping("/")
    public List<Cancha> listarSlash() {
        return canchaService.listarActivas();
    }

    // Si quieres mantener el endpoint /activas, lo dejamos también:
    @GetMapping("/activas")
    public List<Cancha> activas() {
        return canchaService.listarActivas();
    }

    // ====== CRUD ADMIN ======

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cancha> crear(@RequestBody Cancha body) {
        Cancha created = canchaService.crear(body);
        return ResponseEntity
                .created(URI.create("/api/canchas/" + created.getId()))
                .body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Cancha actualizar(@PathVariable Long id, @RequestBody Cancha body) {
        return canchaService.actualizar(id, body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        canchaService.eliminar(id);
    }
}



