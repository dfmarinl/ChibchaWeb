package com.chibchaweb.chibchaweb.pago.domain;

public class ComisionBasica implements EstrategiaComision {

    private static final double PORCENTAJE = 5.0;

    @Override
    public double calcular(double monto) {
        return monto * (PORCENTAJE / 100.0);
    }

    public double getPorcentaje() {
        return PORCENTAJE;
    }
}
