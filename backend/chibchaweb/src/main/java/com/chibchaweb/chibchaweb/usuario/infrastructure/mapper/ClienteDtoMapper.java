package com.chibchaweb.chibchaweb.usuario.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.ClienteResponse;

@Mapper(componentModel = "spring")
public interface ClienteDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "limitesSitios", ignore = true)
    Cliente toDomain(CrearClienteRequest request);

    ClienteResponse toResponse(Cliente cliente);
}
