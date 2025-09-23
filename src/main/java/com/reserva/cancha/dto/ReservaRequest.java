package com.reserva.cancha.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReservaRequest {

    @NotNull(message = "canchaId es obligatorio")
    private Long canchaId;

    @NotNull(message = "inicio es obligatorio")
    @FutureOrPresent(message = "inicio debe ser presente o futuro")
    private LocalDateTime inicio;

    @NotNull(message = "fin es obligatorio")
    @FutureOrPresent(message = "fin debe ser presente o futuro")
    private LocalDateTime fin;

    /**
     * username del usuario que reserva (ej: "user").
     * Lo validamos contra la tabla usuario para que exista.
     */
    @NotNull(message = "usuario es obligatorio")
    private String usuario;

    public Long getCanchaId() {
        return canchaId;
    }

    public void setCanchaId(Long canchaId) {
        this.canchaId = canchaId;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
