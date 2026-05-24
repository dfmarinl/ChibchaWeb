package com.chibchaweb.chibchaweb.distribuidor.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.chibchaweb.chibchaweb.distribuidor.domain.Distribuidor;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request.CrearDistribuidorRequest;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.response.DistribuidorResponse;

@Mapper(componentModel = "spring")
public interface DistribuidorDtoMapper {

    @Mapping(target = "id", ignore = true)
    Distribuidor toDomain(CrearDistribuidorRequest request);

    DistribuidorResponse toResponse(Distribuidor distribuidor);
}
