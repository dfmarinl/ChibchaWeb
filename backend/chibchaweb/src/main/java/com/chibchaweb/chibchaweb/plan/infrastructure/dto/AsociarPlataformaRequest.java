package com.chibchaweb.chibchaweb.plan.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record AsociarPlataformaRequest(
        @NotBlank String plataforma,
        Boolean mysqlIncluido,
        String phpVersion,
        Boolean sqlServerIncluido,
        String iisVersion,
        Boolean pythonIncluido,
        String aspNetVersion
) {
}
