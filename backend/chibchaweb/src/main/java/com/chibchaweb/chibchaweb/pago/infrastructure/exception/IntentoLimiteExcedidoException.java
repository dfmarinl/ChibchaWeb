package com.chibchaweb.chibchaweb.pago.infrastructure.exception;

public class IntentoLimiteExcedidoException extends RuntimeException {

    public IntentoLimiteExcedidoException() {
        super("Has superado el límite de intentos permitidos");
    }
}
