package com.chibchaweb.chibchaweb.acceso.infrastructure.dto;

public record CredencialDto(
        Long id,
        String email,
        int intentosFallidos
) {
}
