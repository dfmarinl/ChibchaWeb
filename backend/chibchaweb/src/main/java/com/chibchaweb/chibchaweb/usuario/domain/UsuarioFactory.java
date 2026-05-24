package com.chibchaweb.chibchaweb.usuario.domain;

import org.springframework.stereotype.Component;

@Component
public abstract class UsuarioFactory {

    public abstract Usuario crearUsuario(Long id, String nombre, String email, String telefono);

    protected void validarDatos(String nombre, String email) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
    }
}
