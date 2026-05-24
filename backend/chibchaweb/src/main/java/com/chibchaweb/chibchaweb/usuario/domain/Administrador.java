package com.chibchaweb.chibchaweb.usuario.domain;

public class Administrador extends Usuario {

    private String nivelAcceso;

    public Administrador() {
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

    @Override
    public String getTipoUsuario() {
        return "ADMINISTRADOR";
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
