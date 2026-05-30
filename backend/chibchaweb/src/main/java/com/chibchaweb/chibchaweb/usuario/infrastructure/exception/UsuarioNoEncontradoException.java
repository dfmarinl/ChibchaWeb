package com.chibchaweb.chibchaweb.usuario.infrastructure.exception;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public static UsuarioNoEncontradoException porId(Long id) {
        return new UsuarioNoEncontradoException("Usuario no encontrado con id: " + id);
    }

    public static UsuarioNoEncontradoException porEmail(String email) {
        return new UsuarioNoEncontradoException("Usuario no encontrado con email: " + email);
    }
}
