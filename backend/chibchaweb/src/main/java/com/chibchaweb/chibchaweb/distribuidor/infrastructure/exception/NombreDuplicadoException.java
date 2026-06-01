package com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception;

public class NombreDuplicadoException extends RuntimeException {

    private final int intentosRestantes;

    public NombreDuplicadoException(String nombre) {
        super("El nombre del distribuidor ya esta registrado: " + nombre);
        this.intentosRestantes = -1;
    }

    public NombreDuplicadoException(String nombre, int intentosRestantes) {
        super("El nombre del distribuidor ya esta registrado: " + nombre);
        this.intentosRestantes = intentosRestantes;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }
}
