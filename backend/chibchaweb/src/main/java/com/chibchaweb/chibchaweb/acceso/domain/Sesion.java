package com.chibchaweb.chibchaweb.acceso.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Sesion {

    private Long id;
    private Long cuentaId;
    private LocalDateTime fechaEmision;
    private LocalDateTime fechaExpiracion;
    private boolean activa;

    public Sesion() {
    }

    public Sesion(Long id, Long cuentaId, int duracionMinutos) {
        if (cuentaId == null) {
            throw new IllegalArgumentException("El id de cuenta no puede ser nulo");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duracion debe ser positiva");
        }
        this.id = id;
        this.cuentaId = cuentaId;
        this.fechaEmision = LocalDateTime.now();
        this.fechaExpiracion = fechaEmision.plusMinutes(duracionMinutos);
        this.activa = true;
    }

    public Sesion(Long id, Long cuentaId, LocalDateTime fechaEmision,
                  LocalDateTime fechaExpiracion, boolean activa) {
        if (cuentaId == null) {
            throw new IllegalArgumentException("El id de cuenta no puede ser nulo");
        }
        if (fechaEmision == null) {
            throw new IllegalArgumentException("La fecha de emision no puede ser nula");
        }
        if (fechaExpiracion == null) {
            throw new IllegalArgumentException("La fecha de expiracion no puede ser nula");
        }
        if (fechaExpiracion.isBefore(fechaEmision)) {
            throw new IllegalArgumentException("La fecha de expiracion debe ser posterior a la emision");
        }
        this.id = id;
        this.cuentaId = cuentaId;
        this.fechaEmision = fechaEmision;
        this.fechaExpiracion = fechaExpiracion;
        this.activa = activa;
    }

    public Long getId() {
        return id;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public boolean isActiva() {
        return activa;
    }

    public boolean estaVigente() {
        return activa && LocalDateTime.now().isBefore(fechaExpiracion);
    }

    public void invalidar() {
        this.activa = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sesion sesion)) return false;
        return id != null && Objects.equals(id, sesion.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Sesion{id=" + id + ", cuentaId=" + cuentaId
                + ", vigente=" + estaVigente() + "}";
    }

}
