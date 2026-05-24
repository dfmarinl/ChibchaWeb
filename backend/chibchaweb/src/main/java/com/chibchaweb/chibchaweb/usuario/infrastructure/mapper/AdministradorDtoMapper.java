package com.chibchaweb.chibchaweb.usuario.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.chibchaweb.chibchaweb.usuario.domain.Administrador;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearAdministradorRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.AdministradorResponse;

@Mapper(componentModel = "spring")
public interface AdministradorDtoMapper {

    @Mapping(target = "id", ignore = true)
    Administrador toDomain(CrearAdministradorRequest request);

    AdministradorResponse toResponse(Administrador administrador);
}
