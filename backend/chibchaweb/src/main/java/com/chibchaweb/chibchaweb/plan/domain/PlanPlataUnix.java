package com.chibchaweb.chibchaweb.plan.domain;

import java.util.HashMap;
import java.util.Map;

public class PlanPlataUnix extends PlanPlata {

    protected PlanPlataUnix() {
    }

    public PlanPlataUnix(Long id, String nombre, double precioMensual,
                         int espacioDisco, int anchoBanda, int cuentasEmail) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail);
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        Map<String, String> caracteristicas = new HashMap<>(super.getCaracteristicas());
        caracteristicas.put("plataforma", "Unix/Linux");
        return caracteristicas;
    }

    @Override
    public String getPlataforma() {
        return "Unix/Linux";
    }
}
