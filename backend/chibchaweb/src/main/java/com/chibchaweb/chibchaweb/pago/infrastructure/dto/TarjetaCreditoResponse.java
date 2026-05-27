package com.chibchaweb.chibchaweb.pago.infrastructure.dto;

public record TarjetaCreditoResponse(
        Long id,
        String titular,
        String numeroEnmascarado,
        String fechaVencimiento,
        String tipoTarjeta
) {}
