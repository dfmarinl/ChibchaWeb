package com.chibchaweb.chibchaweb.plan.infrastructure.dto;

import com.chibchaweb.chibchaweb.pago.infrastructure.dto.PagoResponse;

public record CompraPlanResult(
    PagoResponse pagoResponse,
    Long suscripcionId,
    String fechaFin
) {}
