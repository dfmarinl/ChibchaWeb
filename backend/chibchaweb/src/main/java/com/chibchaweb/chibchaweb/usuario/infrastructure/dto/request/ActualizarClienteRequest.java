package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request;

import jakarta.validation.constraints.Email;

public record ActualizarClienteRequest(
        String nombre,
        @Email String email,
        String telefono,
        String direccion,
        String documentoIdentidad,
        String region
) {}
