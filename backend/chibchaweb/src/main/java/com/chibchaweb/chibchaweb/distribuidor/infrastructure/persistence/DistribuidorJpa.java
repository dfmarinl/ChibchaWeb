package com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.chibchaweb.chibchaweb.distribuidor.domain.NivelDistribuidor;

@Entity
@Table(name = "distribuidor")
public class DistribuidorJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    private String region;

    @Column(name = "codigo_distribuidor", nullable = false, unique = true)
    private String codigoDistribuidor;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_distribuidor", nullable = false)
    private NivelDistribuidor nivelDistribuidor;

    public DistribuidorJpa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCodigoDistribuidor() {
        return codigoDistribuidor;
    }

    public void setCodigoDistribuidor(String codigoDistribuidor) {
        this.codigoDistribuidor = codigoDistribuidor;
    }

    public NivelDistribuidor getNivelDistribuidor() {
        return nivelDistribuidor;
    }

    public void setNivelDistribuidor(NivelDistribuidor nivelDistribuidor) {
        this.nivelDistribuidor = nivelDistribuidor;
    }
}
