package com.chibchaweb.chibchaweb.pago.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comision {

    private Long id;
    private double montoCalculado;
    private LocalDateTime fechaCalculo;
    private EstrategiaComision estrategia;

    protected Comision() {
    }

    public Comision(Long id, EstrategiaComision estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de comision no puede ser nula");
        }
        this.id = id;
        this.estrategia = estrategia;
        this.fechaCalculo = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public double getMontoCalculado() {
        return montoCalculado;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    public EstrategiaComision getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaComision estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de comision no puede ser nula");
        }
        this.estrategia = estrategia;
    }

    public double calcular(double monto) {
        this.montoCalculado = estrategia.calcular(monto);
        this.fechaCalculo = LocalDateTime.now();
        return montoCalculado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comision comision)) return false;
        return id != null && Objects.equals(id, comision.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Comision{id=" + id + ", montoCalculado=" + montoCalculado + "}";
    }
}
