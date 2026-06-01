package com.chibchaweb.chibchaweb.plan.domain;

import java.util.HashMap;
import java.util.Map;

public class PlanPlatinoUnix extends PlanPlatino {

    private boolean mysqlIncluido;
    private String phpVersion;

    protected PlanPlatinoUnix() {
    }

    public PlanPlatinoUnix(Long id, String nombre, double precioMensual,
                           int espacioDisco, int anchoBanda, int cuentasEmail,
                           boolean mysqlIncluido, String phpVersion,
                           int limiteSitios) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail, limiteSitios);
        this.mysqlIncluido = mysqlIncluido;
        this.phpVersion = phpVersion;
    }

    public boolean isMysqlIncluido() {
        return mysqlIncluido;
    }

    public String getPhpVersion() {
        return phpVersion;
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        Map<String, String> caracteristicas = new HashMap<>(super.getCaracteristicas());
        caracteristicas.put("plataforma", "Unix/Linux");
        caracteristicas.put("mysql", mysqlIncluido ? "Incluido" : "No incluido");
        caracteristicas.put("php", phpVersion);
        return caracteristicas;
    }

    @Override
    public String getPlataforma() {
        return "Unix/Linux";
    }
}
