package com.chibchaweb.chibchaweb.usuario.infrastructure.exception;

public class DocumentoDuplicadoException extends RuntimeException {

    private final int intentosRestantes;

    public DocumentoDuplicadoException(String documento) {
        super("El documento de identidad ya esta registrado: " + documento);
        this.intentosRestantes = -1;
    }

    public DocumentoDuplicadoException(String documento, int intentosRestantes) {
        super("El documento de identidad ya esta registrado: " + documento);
        this.intentosRestantes = intentosRestantes;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }
}
