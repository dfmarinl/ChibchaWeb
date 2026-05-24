package com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.chibchaweb.chibchaweb.distribuidor.domain.NivelDistribuidor;

public record CrearDistribuidorRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String region,
        @NotBlank String codigoDistribuidor,
        NivelDistribuidor nivelDistribuidor
) {}
