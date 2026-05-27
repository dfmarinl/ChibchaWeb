package com.chibchaweb.chibchaweb.pago.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record TarjetaCreditoRequest(
        @NotBlank String numero,
        @NotBlank String titular,
        @NotBlank String fechaVencimiento,
        @NotBlank String cvv
) {}
