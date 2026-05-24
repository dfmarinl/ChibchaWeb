package com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception;

public class EmailDuplicadoException extends RuntimeException {

    public EmailDuplicadoException(String email) {
        super("El email ya esta registrado: " + email);
    }
}
