package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrador")
@PrimaryKeyJoinColumn(name = "usuario_id")
@DiscriminatorValue("ADMINISTRADOR")
public class AdministradorJpa extends UsuarioJpa {

    @Column(name = "nivel_acceso", nullable = false)
    private String nivelAcceso;

    public AdministradorJpa() {
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }
}
