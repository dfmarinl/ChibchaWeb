package com.chibchaweb.chibchaweb.pago.infrastructure.dto;

import java.time.LocalDateTime;

public record PagoResponse(
    Long id,
    double monto,
    String referencia,
    String estado,
    LocalDateTime fecha,
    String tarjetaEnmascarada,
    String tipoTarjeta,
    String clienteNombre,
    String periodicidad
) {}
