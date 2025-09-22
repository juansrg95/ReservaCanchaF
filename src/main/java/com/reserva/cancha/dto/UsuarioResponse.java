package com.reserva.cancha.dto;

import java.util.Set;

public record UsuarioResponse(
        Long id,
        String username,
        String nombre,
        String email,
        Set<String> roles
) {}

