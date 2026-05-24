package com.chibchaweb.chibchaweb.pago.domain;

public class ComisionPremium implements EstrategiaComision {

    private static final double PORCENTAJE = 12.0;
    private static final double BONO_ADICIONAL = 500.0;

    @Override
    public double calcular(double monto) {
        return (monto * (PORCENTAJE / 100.0)) + BONO_ADICIONAL;
    }

    public double getPorcentaje() {
        return PORCENTAJE;
    }

    public double getBonoAdicional() {
        return BONO_ADICIONAL;
    }
}
