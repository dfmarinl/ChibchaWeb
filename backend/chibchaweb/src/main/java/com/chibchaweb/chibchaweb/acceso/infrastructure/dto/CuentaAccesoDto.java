package com.chibchaweb.chibchaweb.acceso.infrastructure.dto;

import java.time.LocalDateTime;
import com.chibchaweb.chibchaweb.acceso.domain.EstadoCuenta;

public record CuentaAccesoDto(
        Long id,
        Long usuarioId,
        EstadoCuenta estado,
        LocalDateTime fechaUltimoAcceso,
        RolDto rol,
        CredencialDto credencial
) {
}
