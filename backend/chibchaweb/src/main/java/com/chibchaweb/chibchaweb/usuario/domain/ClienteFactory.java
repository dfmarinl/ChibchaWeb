package com.chibchaweb.chibchaweb.usuario.domain;

import org.springframework.stereotype.Component;

@Component
public class ClienteFactory extends UsuarioFactory {

    @Override
    public Cliente crearUsuario(Long id, String nombre, String email, String telefono) {
        throw new UnsupportedOperationException(
            "Use crearCliente(id, nombre, email, telefono, direccion, documentoIdentidad)");
    }

    public Cliente crearCliente(Long id, String nombre, String email, String telefono,
                                 String direccion, String documentoIdentidad) {
        return new Cliente(id, nombre, email, telefono, direccion, documentoIdentidad);
    }

    public Cliente crearCliente(Long id, String nombre, String email, String telefono,
                                 String direccion, String documentoIdentidad, String region) {
        return new Cliente(id, nombre, email, telefono, direccion, documentoIdentidad, region);
    }
}
