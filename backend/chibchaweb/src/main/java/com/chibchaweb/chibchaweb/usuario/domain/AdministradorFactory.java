package com.chibchaweb.chibchaweb.usuario.domain;

import org.springframework.stereotype.Component;

@Component
public class AdministradorFactory extends UsuarioFactory {

    @Override
    public Administrador crearUsuario(Long id, String nombre, String email, String telefono) {
        throw new UnsupportedOperationException(
            "Use crearAdministrador(id, nombre, email, telefono, nivelAcceso)");
    }

    public Administrador crearAdministrador(Long id, String nombre, String email, String telefono,
                                             String nivelAcceso) {
        return new Administrador(id, nombre, email, telefono, nivelAcceso);
    }
}
