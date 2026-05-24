package com.chibchaweb.chibchaweb.plan.domain;

import java.util.Map;

public class PlanOro extends HostingPlan {

    protected PlanOro() {
    }

    public PlanOro(Long id, String nombre, double precioMensual,
                   int espacioDisco, int anchoBanda, int cuentasEmail) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail);
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        return Map.of(
            "plan", "Oro",
            "soporte", "Prioritario",
            "ssl", "Incluido",
            "cpu", "4 nucleos",
            "ram", "8 GB"
        );
    }

    @Override
    public String getPlataforma() {
        return "Estandar";
    }
}
