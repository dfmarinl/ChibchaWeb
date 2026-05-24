package com.chibchaweb.chibchaweb.acceso.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroClienteRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String contrasena,
        String telefono,
        String direccion,
        @NotBlank String documentoIdentidad,
        String region
) {}
