package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response;

public record AdministradorResponse(
        Long id,
        String nombre,
        String email,
        String telefono,
        String nivelAcceso
) {}
