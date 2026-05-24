package com.chibchaweb.chibchaweb.acceso.infrastructure.dto;

public record LoginRequest(
        String email,
        String contrasena
) {
}
