package com.chibchaweb.chibchaweb.plan.domain;

import java.util.HashMap;
import java.util.Map;

public class PlanOroWindows extends PlanOro {

    private String aspNetVersion;

    protected PlanOroWindows() {
    }

    public PlanOroWindows(Long id, String nombre, double precioMensual,
                          int espacioDisco, int anchoBanda, int cuentasEmail,
                          String aspNetVersion) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail);
        this.aspNetVersion = aspNetVersion;
    }

    public String getAspNetVersion() {
        return aspNetVersion;
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        Map<String, String> caracteristicas = new HashMap<>(super.getCaracteristicas());
        caracteristicas.put("plataforma", "Windows");
        caracteristicas.put("aspNet", aspNetVersion);
        return caracteristicas;
    }

    @Override
    public String getPlataforma() {
        return "Windows";
    }
}
