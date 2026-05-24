package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request;

import jakarta.validation.constraints.Email;

public record ActualizarEmpleadoRequest(
        String nombre,
        @Email String email,
        String telefono,
        String cargo,
        String departamento,
        Double salario
) {}
