package com.chibchaweb.chibchaweb.acceso.infrastructure.dto;

public record LoginResponse(
        String token,
        Long sesionId,
        String email,
        String rol
) {
}
