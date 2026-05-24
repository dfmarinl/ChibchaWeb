package com.chibchaweb.chibchaweb.plan.domain;

import java.util.Map;

public class PlanPlatino extends HostingPlan {

    protected PlanPlatino() {
    }

    public PlanPlatino(Long id, String nombre, double precioMensual,
                       int espacioDisco, int anchoBanda, int cuentasEmail) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail);
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        return Map.of(
            "plan", "Platino",
            "soporte", "Premium 24/7",
            "ssl", "Incluido",
            "cpu", "8 nucleos",
            "ram", "16 GB",
            "backup", "Diario automatico"
        );
    }

    @Override
    public String getPlataforma() {
        return "Estandar";
    }
}
