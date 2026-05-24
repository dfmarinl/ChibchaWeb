package com.chibchaweb.chibchaweb.usuario.infrastructure.exception;

public class TipoUsuarioInvalidoException extends RuntimeException {

    public TipoUsuarioInvalidoException(String tipo) {
        super("Tipo de usuario invalido: " + tipo);
    }
}
