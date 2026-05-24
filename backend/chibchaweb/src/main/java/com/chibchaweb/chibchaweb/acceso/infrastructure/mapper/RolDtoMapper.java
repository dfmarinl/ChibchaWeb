package com.chibchaweb.chibchaweb.acceso.infrastructure.mapper;

import org.mapstruct.Mapper;
import com.chibchaweb.chibchaweb.acceso.domain.Rol;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.RolDto;

@Mapper(componentModel = "spring", uses = PermisoDtoMapper.class)
public interface RolDtoMapper {

    RolDto toDto(Rol domain);

    Rol toDomain(RolDto dto);
}
