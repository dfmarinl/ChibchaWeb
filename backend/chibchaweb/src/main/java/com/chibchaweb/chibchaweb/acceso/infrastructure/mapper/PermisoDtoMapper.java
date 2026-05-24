package com.chibchaweb.chibchaweb.acceso.infrastructure.mapper;

import org.mapstruct.Mapper;
import com.chibchaweb.chibchaweb.acceso.domain.Permiso;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.PermisoDto;

@Mapper(componentModel = "spring")
public interface PermisoDtoMapper {

    PermisoDto toDto(Permiso domain);

    Permiso toDomain(PermisoDto dto);
}
