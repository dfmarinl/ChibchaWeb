package com.chibchaweb.chibchaweb.usuario.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.chibchaweb.chibchaweb.usuario.domain.Empleado;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearEmpleadoRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.EmpleadoResponse;

@Mapper(componentModel = "spring")
public interface EmpleadoDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaContratacion", ignore = true)
    Empleado toDomain(CrearEmpleadoRequest request);

    EmpleadoResponse toResponse(Empleado empleado);
}
