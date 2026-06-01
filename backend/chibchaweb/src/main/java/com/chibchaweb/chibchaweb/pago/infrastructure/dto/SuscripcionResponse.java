package com.chibchaweb.chibchaweb.pago.infrastructure.dto;

import java.time.LocalDateTime;

public record SuscripcionResponse(
    Long id,
    Long planId,
    String planNombre,
    String periodicidad,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    String estado,
    Long pagoId
) {}
