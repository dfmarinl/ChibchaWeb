package com.chibchaweb.chibchaweb.acceso.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class CuentaAcceso {

    private Long id;
    private Long usuarioId;
    private EstadoCuenta estado;
    private LocalDateTime fechaUltimoAcceso;
    private Rol rol;
    private Credencial credencial;

    protected CuentaAcceso() {
    }

    public CuentaAcceso(Long id, Long usuarioId, Rol rol, Credencial credencial) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El id de usuario no puede ser nulo");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        if (credencial == null) {
            throw new IllegalArgumentException("La credencial no puede ser nula");
        }
        this.id = id;
        this.usuarioId = usuarioId;
        this.estado = EstadoCuenta.ACTIVA;
        this.fechaUltimoAcceso = null;
        this.rol = rol;
        this.credencial = credencial;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public LocalDateTime getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public Rol getRol() {
        return rol;
    }

    public Credencial getCredencial() {
        return credencial;
    }

    public boolean autenticar(String contrasena, PasswordEncoder encoder) {
        if (contrasena == null || contrasena.isBlank()) {
            return false;
        }
        if (estado != EstadoCuenta.ACTIVA) {
            return false;
        }
        if (credencial.estaBloqueada()) {
            return false;
        }
        boolean valida = credencial.verificarContrasena(contrasena, encoder);
        if (valida) {
            credencial.reiniciarIntentos();
            this.fechaUltimoAcceso = LocalDateTime.now();
        } else {
            credencial.registrarIntentoFallido();
            if (credencial.estaBloqueada()) {
                this.estado = EstadoCuenta.BLOQUEADA;
            }
        }
        return valida;
    }

    public boolean estaActiva() {
        return estado == EstadoCuenta.ACTIVA;
    }

    public void bloquear() {
        if (estado == EstadoCuenta.BLOQUEADA) {
            throw new IllegalStateException("La cuenta ya se encuentra bloqueada");
        }
        this.estado = EstadoCuenta.BLOQUEADA;
    }

    public void suspender() {
        if (estado == EstadoCuenta.SUSPENDIDA) {
            throw new IllegalStateException("La cuenta ya se encuentra suspendida");
        }
        this.estado = EstadoCuenta.SUSPENDIDA;
    }

    public void activar() {
        if (estado == EstadoCuenta.ACTIVA) {
            throw new IllegalStateException("La cuenta ya se encuentra activa");
        }
        this.estado = EstadoCuenta.ACTIVA;
        this.credencial.reiniciarIntentos();
    }

    public boolean tienePermiso(Permiso permiso) {
        return rol.tienePermiso(permiso);
    }

    public boolean tienePermiso(String nombrePermiso) {
        return rol.tienePermiso(nombrePermiso);
    }

    public void cambiarRol(Rol nuevoRol) {
        if (nuevoRol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        this.rol = nuevoRol;
    }

    public void cambiarCredencial(Credencial nuevaCredencial) {
        if (nuevaCredencial == null) {
            throw new IllegalArgumentException("La credencial no puede ser nula");
        }
        this.credencial = nuevaCredencial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CuentaAcceso that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CuentaAcceso{id=" + id + ", usuarioId=" + usuarioId
                + ", estado=" + estado + ", rol=" + rol.getNombre() + "}";
    }

}
