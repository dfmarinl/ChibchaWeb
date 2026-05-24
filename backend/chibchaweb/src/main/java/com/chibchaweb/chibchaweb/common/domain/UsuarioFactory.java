package com.chibchaweb.chibchaweb.common.domain;

public class UsuarioFactory {

    public Usuario crearUsuario(Long id, String nombre, String email,
                                String telefono, String direccion,
                                String documentoIdentidad, String cargo,
                                String area, String nivelAcceso) {

        if (documentoIdentidad != null && !documentoIdentidad.isBlank()) {
            return new Cliente(id, nombre, email, telefono,
                               direccion, documentoIdentidad);
        }

        if (cargo != null && !cargo.isBlank()) {
            return new Empleado(id, nombre, email, telefono,
                                cargo, area);
        }

        if (nivelAcceso != null && !nivelAcceso.isBlank()) {
            return new Administrador(id, nombre, email, telefono,
                                     nivelAcceso);
        }

        throw new IllegalArgumentException(
            "No se pudo determinar el tipo de usuario: " +
            "faltan datos distintivos (documentoIdentidad, cargo o nivelAcceso)");
    }
}
