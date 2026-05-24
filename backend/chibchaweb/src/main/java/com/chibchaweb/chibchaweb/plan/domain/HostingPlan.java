package com.chibchaweb.chibchaweb.plan.domain;

import java.util.Map;
import java.util.Objects;

public abstract class HostingPlan {

    protected Long id;
    protected String nombre;
    protected double precioMensual;
    protected int espacioDisco;
    protected int anchoBanda;
    protected int cuentasEmail;

    protected HostingPlan() {
    }

    protected HostingPlan(Long id, String nombre, double precioMensual,
                          int espacioDisco, int anchoBanda, int cuentasEmail) {
        this.id = id;
        this.nombre = nombre;
        this.precioMensual = precioMensual;
        this.espacioDisco = espacioDisco;
        this.anchoBanda = anchoBanda;
        this.cuentasEmail = cuentasEmail;
    }

    public abstract Map<String, String> getCaracteristicas();

    public abstract String getPlataforma();

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioMensual() {
        return precioMensual;
    }

    public int getEspacioDisco() {
        return espacioDisco;
    }

    public int getAnchoBanda() {
        return anchoBanda;
    }

    public int getCuentasEmail() {
        return cuentasEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HostingPlan that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", nombre='" + nombre + "', precio=" + precioMensual + "}";
    }
}
