package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import com.chibchaweb.chibchaweb.acceso.domain.EstadoCuenta;

@Entity
@Table(name = "cuenta_acceso")
public class CuentaAccesoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estado;

    @Column(name = "fecha_ultimo_acceso")
    private LocalDateTime fechaUltimoAcceso;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private RolJpa rol;

    @OneToOne
    @JoinColumn(name = "credencial_id", nullable = false, unique = true)
    private CredencialJpa credencial;

    public CuentaAccesoJpa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }

    public RolJpa getRol() {
        return rol;
    }

    public void setRol(RolJpa rol) {
        this.rol = rol;
    }

    public CredencialJpa getCredencial() {
        return credencial;
    }

    public void setCredencial(CredencialJpa credencial) {
        this.credencial = credencial;
    }
}
