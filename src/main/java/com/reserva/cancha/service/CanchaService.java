package com.reserva.cancha.service;

import com.reserva.cancha.model.Cancha;

import java.util.List;

public interface CanchaService {
    List<Cancha> listarActivas();
    Cancha crear(Cancha cancha);
    Cancha actualizar(Long id, Cancha cancha);
    void   eliminar(Long id);
}


