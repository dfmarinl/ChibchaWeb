package com.chibchaweb.chibchaweb.pago.domain;

import java.util.Map;

public class PagoVisa implements EstrategiaPago {

    private String codigoAutorizacion;

    public PagoVisa() {
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    @Override
    public ResultadoPago procesarPago(Map<String, Object> datos, double monto) {
        this.codigoAutorizacion = "VISA-" + System.currentTimeMillis();
        return new ResultadoPago(true, "00", "Pago VISA aprobado", codigoAutorizacion);
    }

    @Override
    public boolean validarDatos(Map<String, Object> datos) {
        return datos != null && datos.containsKey("numeroTarjeta");
    }
}
