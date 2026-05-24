package com.chibchaweb.chibchaweb.pago.domain;

import java.util.Map;

public class PagoDiners implements EstrategiaPago {

    @Override
    public ResultadoPago procesarPago(Map<String, Object> datos, double monto) {
        String idTransaccion = "DINERS-" + System.currentTimeMillis();
        return new ResultadoPago(true, "00", "Pago Diners aprobado", idTransaccion);
    }

    @Override
    public boolean validarDatos(Map<String, Object> datos) {
        return datos != null && datos.containsKey("numeroTarjeta");
    }
}
