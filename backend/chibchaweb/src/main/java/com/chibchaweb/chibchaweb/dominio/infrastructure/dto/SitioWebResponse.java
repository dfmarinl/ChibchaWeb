package com.chibchaweb.chibchaweb.dominio.infrastructure.dto;

import java.time.LocalDateTime;

public record SitioWebResponse(
        Long id,
        String urlSitio,
        boolean estadoActivo,
        LocalDateTime fechaCreacion
) {}
