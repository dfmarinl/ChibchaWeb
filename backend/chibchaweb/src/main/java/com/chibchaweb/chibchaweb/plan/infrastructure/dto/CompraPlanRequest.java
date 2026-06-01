package com.chibchaweb.chibchaweb.plan.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompraPlanRequest(
    @NotNull Long tarjetaId,
    @NotBlank String periodicidad
) {}
