package com.reserva.cancha.dto;

import java.time.LocalDateTime;

public class ReservaResponse {
    private Long id;
    private Long canchaId;
    private String canchaNombre;
    private String usuario;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String estado;

    public ReservaResponse() {}

    public ReservaResponse(Long id, Long canchaId, String canchaNombre, String usuario,
                           LocalDateTime inicio, LocalDateTime fin, String estado) {
        this.id = id;
        this.canchaId = canchaId;
        this.canchaNombre = canchaNombre;
        this.usuario = usuario;
        this.inicio = inicio;
        this.fin = fin;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public Long getCanchaId() { return canchaId; }
    public String getCanchaNombre() { return canchaNombre; }
    public String getUsuario() { return usuario; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFin() { return fin; }
    public String getEstado() { return estado; }

    public void setId(Long id) { this.id = id; }
    public void setCanchaId(Long canchaId) { this.canchaId = canchaId; }
    public void setCanchaNombre(String canchaNombre) { this.canchaNombre = canchaNombre; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public void setEstado(String estado) { this.estado = estado; }
}
