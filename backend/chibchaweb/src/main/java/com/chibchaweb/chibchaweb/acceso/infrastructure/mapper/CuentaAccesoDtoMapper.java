package com.chibchaweb.chibchaweb.acceso.infrastructure.mapper;

import org.mapstruct.Mapper;
import com.chibchaweb.chibchaweb.acceso.domain.CuentaAcceso;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.CuentaAccesoDto;

@Mapper(componentModel = "spring", uses = {RolDtoMapper.class, CredencialDtoMapper.class})
public interface CuentaAccesoDtoMapper {

    CuentaAccesoDto toDto(CuentaAcceso domain);

    CuentaAcceso toDomain(CuentaAccesoDto dto);
}
