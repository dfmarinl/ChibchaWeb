package com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nombre,
        String email,
        String telefono,
        LocalDateTime fechaRegistro,
        String direccion,
        String documentoIdentidad,
        String region,
        int limitesSitios
) {}
