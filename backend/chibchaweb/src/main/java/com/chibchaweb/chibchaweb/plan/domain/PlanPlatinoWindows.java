package com.chibchaweb.chibchaweb.plan.domain;

import java.util.HashMap;
import java.util.Map;

public class PlanPlatinoWindows extends PlanPlatino {

    private boolean sqlServerIncluido;
    private String iisVersion;

    protected PlanPlatinoWindows() {
    }

    public PlanPlatinoWindows(Long id, String nombre, double precioMensual,
                              int espacioDisco, int anchoBanda, int cuentasEmail,
                              boolean sqlServerIncluido, String iisVersion) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail);
        this.sqlServerIncluido = sqlServerIncluido;
        this.iisVersion = iisVersion;
    }

    public boolean isSqlServerIncluido() {
        return sqlServerIncluido;
    }

    public String getIisVersion() {
        return iisVersion;
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        Map<String, String> caracteristicas = new HashMap<>(super.getCaracteristicas());
        caracteristicas.put("plataforma", "Windows");
        caracteristicas.put("sqlServer", sqlServerIncluido ? "Incluido" : "No incluido");
        caracteristicas.put("iis", iisVersion);
        return caracteristicas;
    }

    @Override
    public String getPlataforma() {
        return "Windows";
    }
}
