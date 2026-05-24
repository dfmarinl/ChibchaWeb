package com.chibchaweb.chibchaweb.common.domain;

public class Administrador extends Usuario {

    private String nivelAcceso;

    protected Administrador() {
    }

    public Administrador(Long id, String nombre, String email, String telefono,
                         String nivelAcceso) {
        super(id, nombre, email, telefono);
        validarNivelAcceso(nivelAcceso);
        this.nivelAcceso = nivelAcceso.trim().toUpperCase();
    }

    private void validarNivelAcceso(String nivelAcceso) {
        if (nivelAcceso == null || nivelAcceso.isBlank()) {
            throw new IllegalArgumentException("El nivel de acceso no puede estar vacio");
        }
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void cambiarNivelAcceso(String nuevoNivel) {
        if (nuevoNivel == null || nuevoNivel.isBlank()) {
            throw new IllegalArgumentException("El nivel de acceso no puede estar vacio");
        }
        this.nivelAcceso = nuevoNivel.trim().toUpperCase();
    }

}
