package com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception;

public class DistribuidorNoEncontradoException extends RuntimeException {

    public DistribuidorNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public static DistribuidorNoEncontradoException porId(Long id) {
        return new DistribuidorNoEncontradoException("Distribuidor no encontrado con id: " + id);
    }
}
