package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearEmpleadoRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String telefono,
        @NotBlank String cargo,
        String departamento,
        @NotNull Double salario
) {}
