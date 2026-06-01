package com.chibchaweb.chibchaweb.plan.domain;

import java.util.Map;

public class PlanPlata extends HostingPlan {

    protected PlanPlata() {
    }

    public PlanPlata(Long id, String nombre, double precioMensual,
                     int espacioDisco, int anchoBanda, int cuentasEmail,
                     int limiteSitios) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail, limiteSitios);
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        return Map.of(
            "plan", "Plata",
            "soporte", "Estandar",
            "ssl", "Opcional",
            "cpu", "2 nucleos",
            "ram", "4 GB"
        );
    }

    @Override
    public String getPlataforma() {
        return "Estandar";
    }
}
