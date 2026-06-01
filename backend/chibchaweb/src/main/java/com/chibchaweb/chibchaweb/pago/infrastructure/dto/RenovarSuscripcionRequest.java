package com.chibchaweb.chibchaweb.pago.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RenovarSuscripcionRequest(
    @NotBlank String periodicidad,
    @NotNull Long tarjetaId
) {}
