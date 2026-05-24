package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        String telefono,
        LocalDateTime fechaRegistro,
        String tipoUsuario
) {}
