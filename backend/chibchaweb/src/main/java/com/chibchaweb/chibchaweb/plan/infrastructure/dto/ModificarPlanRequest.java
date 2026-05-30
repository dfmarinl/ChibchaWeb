package com.chibchaweb.chibchaweb.plan.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ModificarPlanRequest(
        @NotBlank String nombre,
        @Positive double precioMensual,
        @Positive int espacioDisco,
        @Positive int anchoBanda,
        @Positive int cuentasEmail,
        Boolean mysqlIncluido,
        String phpVersion,
        Boolean sqlServerIncluido,
        String iisVersion,
        Boolean pythonIncluido,
        String aspNetVersion
) {
}
