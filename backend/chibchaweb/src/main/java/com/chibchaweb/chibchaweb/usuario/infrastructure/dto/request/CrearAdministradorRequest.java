package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CrearAdministradorRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String telefono,
        @NotBlank String nivelAcceso
) {}
