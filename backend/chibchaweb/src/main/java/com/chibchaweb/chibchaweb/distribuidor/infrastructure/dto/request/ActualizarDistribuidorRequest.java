package com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

public record ActualizarDistribuidorRequest(
        String nombre,
        @Email String email,
        String region,
        String codigoDistribuidor,
        @Min(0) Integer maxDominios
) {}
