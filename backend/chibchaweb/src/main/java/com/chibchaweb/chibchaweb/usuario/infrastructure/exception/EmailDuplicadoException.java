package com.chibchaweb.chibchaweb.usuario.infrastructure.exception;

public class EmailDuplicadoException extends RuntimeException {

    private final int intentosRestantes;

    public EmailDuplicadoException(String email) {
        super("El email ya esta registrado: " + email);
        this.intentosRestantes = -1;
    }

    public EmailDuplicadoException(String email, int intentosRestantes) {
        super("El email ya esta registrado: " + email);
        this.intentosRestantes = intentosRestantes;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }
}
