package com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.response;

import com.chibchaweb.chibchaweb.distribuidor.domain.NivelDistribuidor;

public record DistribuidorResponse(
        Long id,
        String nombre,
        String email,
        String region,
        String codigoDistribuidor,
        NivelDistribuidor nivelDistribuidor
) {}
