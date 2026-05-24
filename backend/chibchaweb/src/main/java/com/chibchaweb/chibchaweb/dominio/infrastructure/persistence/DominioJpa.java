package com.chibchaweb.chibchaweb.dominio.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import com.chibchaweb.chibchaweb.dominio.domain.EstadoDominio;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpa;

@Entity
@Table(name = "dominio")
public class DominioJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String extension;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDominio estado;

    @ManyToOne
    @JoinColumn(name = "propietario_id")
    private ClienteJpa propietario;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_vencimiento")
    private LocalDateTime fechaVencimiento;

    protected DominioJpa() {
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public EstadoDominio getEstado() {
        return estado;
    }

    public void setEstado(EstadoDominio estado) {
        this.estado = estado;
    }

    public ClienteJpa getPropietario() {
        return propietario;
    }

    public void setPropietario(ClienteJpa propietario) {
        this.propietario = propietario;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
