package com.chibchaweb.chibchaweb.plan.infrastructure.dto;

import java.util.Map;

public record PlanHostingResponse(
        Long id,
        String nombre,
        double precioMensual,
        int espacioDisco,
        int anchoBanda,
        int cuentasEmail,
        String tipoPlan,
        String plataforma,
        Boolean mysqlIncluido,
        String phpVersion,
        Boolean sqlServerIncluido,
        String iisVersion,
        Boolean pythonIncluido,
        String aspNetVersion,
        Map<String, String> caracteristicas
) {
}
