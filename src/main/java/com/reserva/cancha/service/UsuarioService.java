package com.reserva.cancha.service;

import com.reserva.cancha.dto.UsuarioRequest;
import com.reserva.cancha.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse registrarUsuarioNormal(UsuarioRequest req); // ROLE_USER
    List<UsuarioResponse> listar();
    UsuarioResponse obtener(Long id);
    void eliminar(Long id);
}

