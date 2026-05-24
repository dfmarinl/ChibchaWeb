package com.chibchaweb.chibchaweb.acceso.infrastructure.mapper;

import org.mapstruct.Mapper;
import com.chibchaweb.chibchaweb.acceso.domain.Sesion;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.SesionDto;

@Mapper(componentModel = "spring")
public interface SesionDtoMapper {

    SesionDto toDto(Sesion domain);

    Sesion toDomain(SesionDto dto);
}
