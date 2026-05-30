package com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CrearDistribuidorRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String region,
        @NotBlank String codigoDistribuidor,
        @Min(0) int maxDominios
) {}
