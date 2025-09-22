package com.reserva.cancha.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(min = 4, max = 40) String username,
        @NotBlank @Size(min = 6, max = 60) String password,
        @NotBlank @Size(min = 2, max = 80) String nombre,
        @Email String email
) {}

