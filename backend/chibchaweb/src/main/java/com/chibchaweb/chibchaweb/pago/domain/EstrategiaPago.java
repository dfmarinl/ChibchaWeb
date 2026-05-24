package com.chibchaweb.chibchaweb.pago.domain;

import java.util.Map;

public interface EstrategiaPago {

    ResultadoPago procesarPago(Map<String, Object> datos, double monto);

    boolean validarDatos(Map<String, Object> datos);
}
