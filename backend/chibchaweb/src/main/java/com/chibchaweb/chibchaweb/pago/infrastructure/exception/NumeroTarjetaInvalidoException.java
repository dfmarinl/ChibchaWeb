package com.chibchaweb.chibchaweb.pago.infrastructure.exception;

public class NumeroTarjetaInvalidoException extends RuntimeException {

    private final int intentosRestantes;

    public NumeroTarjetaInvalidoException(String mensaje, int intentosRestantes) {
        super(mensaje);
        this.intentosRestantes = intentosRestantes;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }
}
