package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request;

import jakarta.validation.constraints.Email;

public record ActualizarAdministradorRequest(
        String nombre,
        @Email String email,
        String telefono,
        String nivelAcceso
) {}
