package com.chibchaweb.chibchaweb.pago.domain;

import java.util.Map;

public class PagoMastercard implements EstrategiaPago {

    @Override
    public ResultadoPago procesarPago(Map<String, Object> datos, double monto) {
        String idTransaccion = "MC-" + System.currentTimeMillis();
        return new ResultadoPago(true, "00", "Pago Mastercard aprobado", idTransaccion);
    }

    @Override
    public boolean validarDatos(Map<String, Object> datos) {
        return datos != null && datos.containsKey("numeroTarjeta");
    }
}
