package com.chibchaweb.chibchaweb.plan.domain;

import java.util.HashMap;
import java.util.Map;

public class PlanOroUnix extends PlanOro {

    private boolean pythonIncluido;

    protected PlanOroUnix() {
    }

    public PlanOroUnix(Long id, String nombre, double precioMensual,
                       int espacioDisco, int anchoBanda, int cuentasEmail,
                       boolean pythonIncluido) {
        super(id, nombre, precioMensual, espacioDisco, anchoBanda, cuentasEmail);
        this.pythonIncluido = pythonIncluido;
    }

    public boolean isPythonIncluido() {
        return pythonIncluido;
    }

    @Override
    public Map<String, String> getCaracteristicas() {
        Map<String, String> caracteristicas = new HashMap<>(super.getCaracteristicas());
        caracteristicas.put("plataforma", "Unix/Linux");
        caracteristicas.put("python", pythonIncluido ? "Incluido" : "No incluido");
        return caracteristicas;
    }

    @Override
    public String getPlataforma() {
        return "Unix/Linux";
    }
}
