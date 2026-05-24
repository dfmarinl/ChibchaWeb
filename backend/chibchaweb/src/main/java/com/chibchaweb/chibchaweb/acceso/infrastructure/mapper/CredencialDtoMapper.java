package com.chibchaweb.chibchaweb.acceso.infrastructure.mapper;

import org.mapstruct.Mapper;
import com.chibchaweb.chibchaweb.acceso.domain.Credencial;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.CredencialDto;

@Mapper(componentModel = "spring")
public interface CredencialDtoMapper {

    CredencialDto toDto(Credencial domain);

    Credencial toDomain(CredencialDto dto);
}
