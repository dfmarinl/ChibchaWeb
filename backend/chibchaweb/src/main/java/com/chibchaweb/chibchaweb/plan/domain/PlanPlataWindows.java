package com.chibchaweb.chibchaweb.plan.domain;

import java.util.HashMap;
import java.util.Map;

public class PlanPlataWindows extends PlanPlata {

    protected PlanPlataWindows() {
    }

    public PlanPlataWindows(Long id, String nombre, double precioMensual,
                            int espacioDisco, int anchoBanda, int cuentasEmail,
                            int limiteSitios) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail, limiteSitios);
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        Map<String, String> caracteristicas = new HashMap<>(super.getCaracteristicas());
        caracteristicas.put("plataforma", "Windows");
        return caracteristicas;
    }

    @Override
    public String getPlataforma() {
        return "Windows";
    }
}
