package com.chibchaweb.chibchaweb.acceso.infrastructure.dto;

import java.time.LocalDateTime;

public record SesionDto(
        Long id,
        Long cuentaId,
        LocalDateTime fechaEmision,
        LocalDateTime fechaExpiracion,
        boolean activa
) {
}
