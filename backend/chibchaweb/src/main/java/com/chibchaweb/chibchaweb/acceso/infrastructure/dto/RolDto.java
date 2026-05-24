package com.chibchaweb.chibchaweb.acceso.infrastructure.dto;

import java.util.Set;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;

public record RolDto(
        Long id,
        NombreRol nombre,
        Set<PermisoDto> permisos
) {
}
