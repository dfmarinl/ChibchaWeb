package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "usuario_id")
@DiscriminatorValue("CLIENTE")
public class ClienteJpa extends UsuarioJpa {

    @Column(name = "documento_identidad", nullable = false, unique = true)
    private String documentoIdentidad;

    private String direccion;

    private String region;

    @Column(name = "limites_sitios")
    private int limitesSitios;

    public ClienteJpa() {
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getLimitesSitios() {
        return limitesSitios;
    }

    public void setLimitesSitios(int limitesSitios) {
        this.limitesSitios = limitesSitios;
    }
}
