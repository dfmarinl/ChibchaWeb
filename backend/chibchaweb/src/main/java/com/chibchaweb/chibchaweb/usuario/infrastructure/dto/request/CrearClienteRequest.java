package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CrearClienteRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String telefono,
        String direccion,
        @NotBlank String documentoIdentidad,
        String region
) {}
