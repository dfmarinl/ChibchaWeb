package com.chibchaweb.chibchaweb.common.domain;

import java.time.LocalDateTime;

public class Cliente extends Usuario {

    private String direccion;
    private String documentoIdentidad;
    private LocalDateTime fechaRegistro;

    protected Cliente() {
    }

    public Cliente(Long id, String nombre, String email, String telefono,
                   String direccion, String documentoIdentidad) {
        super(id, nombre, email, telefono);
        validarDocumentoIdentidad(documentoIdentidad);
        this.direccion = (direccion != null) ? direccion.trim() : null;
        this.documentoIdentidad = documentoIdentidad.trim();
        this.fechaRegistro = LocalDateTime.now();
    }

    private void validarDocumentoIdentidad(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento de identidad no puede estar vacio");
        }
    }

    public String getDireccion() {
        return direccion;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void actualizarDireccion(String nuevaDireccion) {
        if (nuevaDireccion == null || nuevaDireccion.isBlank()) {
            throw new IllegalArgumentException("La direccion no puede estar vacia");
        }
        this.direccion = nuevaDireccion.trim();
    }

}
