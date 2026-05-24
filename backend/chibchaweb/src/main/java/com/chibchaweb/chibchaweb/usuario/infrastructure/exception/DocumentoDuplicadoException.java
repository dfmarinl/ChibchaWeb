package com.chibchaweb.chibchaweb.usuario.infrastructure.exception;

public class DocumentoDuplicadoException extends RuntimeException {

    public DocumentoDuplicadoException(String documento) {
        super("El documento de identidad ya esta registrado: " + documento);
    }
}
