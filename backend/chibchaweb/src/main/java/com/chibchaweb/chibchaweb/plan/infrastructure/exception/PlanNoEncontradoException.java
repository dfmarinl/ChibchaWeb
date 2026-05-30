package com.chibchaweb.chibchaweb.plan.infrastructure.exception;

public class PlanNoEncontradoException extends RuntimeException {

    public PlanNoEncontradoException(Long id) {
        super("Plan de hosting no encontrado con id: " + id);
    }
}
