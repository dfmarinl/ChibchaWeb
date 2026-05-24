package com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import com.chibchaweb.chibchaweb.distribuidor.domain.NivelDistribuidor;

public record ActualizarDistribuidorRequest(
        String nombre,
        @Email String email,
        String region,
        String codigoDistribuidor,
        NivelDistribuidor nivelDistribuidor
) {}
