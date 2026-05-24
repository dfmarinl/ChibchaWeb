package com.chibchaweb.chibchaweb.acceso.infrastructure.dto;

public record LoginResponse(
        String token,
        Long sesionId,
        Long usuarioId,
        String email,
        String rol
) {
}
