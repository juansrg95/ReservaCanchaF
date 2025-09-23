
package com.reserva.cancha.controller;

import com.reserva.cancha.dto.ReservaRequest;
import com.reserva.cancha.dto.ReservaResponse;
import com.reserva.cancha.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // GET /api/reservas?canchaId=&desde=&hasta=
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ReservaResponse> listar(
            @RequestParam(required = false) Long canchaId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return reservaService.listar(Optional.ofNullable(canchaId),
                Optional.ofNullable(desde), Optional.ofNullable(hasta));
    }

    // POST /api/reservas
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ReservaResponse crear(@Valid @RequestBody ReservaRequest request, Authentication auth) {
        // Si quieres forzar que el usuario autenticado sea el dueño:
        // request.setUsuario(auth.getName());
        return reservaService.crear(request);
    }

    // DELETE /api/reservas/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void eliminar(@PathVariable Long id, Authentication auth) {
        boolean esAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        reservaService.eliminar(id, auth.getName(), esAdmin);
    }
}

