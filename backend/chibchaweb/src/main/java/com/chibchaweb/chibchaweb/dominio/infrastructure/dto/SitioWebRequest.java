package com.chibchaweb.chibchaweb.dominio.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SitioWebRequest(
        @NotBlank @Pattern(regexp = "^[a-z0-9]([a-z0-9-]*[a-z0-9])?$",
                message = "Solo letras minúsculas, números y guiones") String urlSitio
) {}
