package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response;

import java.time.LocalDateTime;

public record EmpleadoResponse(
        Long id,
        String nombre,
        String email,
        String telefono,
        LocalDateTime fechaRegistro,
        String cargo,
        String departamento,
        double salario,
        LocalDateTime fechaContratacion
) {}
