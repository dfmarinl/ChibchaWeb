package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.TarjetaCreditoJpa;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "usuario_id")
@DiscriminatorValue("CLIENTE")
public class ClienteJpa extends UsuarioJpa {

    @Column(name = "documento_identidad", nullable = false, unique = true)
    private String documentoIdentidad;

    private String direccion;

    private String region;

    @OneToMany(mappedBy = "cliente")
    private List<TarjetaCreditoJpa> tarjetas = new ArrayList<>();

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

    public List<TarjetaCreditoJpa> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<TarjetaCreditoJpa> tarjetas) {
        this.tarjetas = tarjetas;
    }
}
