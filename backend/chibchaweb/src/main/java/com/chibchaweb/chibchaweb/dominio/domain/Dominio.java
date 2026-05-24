package com.chibchaweb.chibchaweb.dominio.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.chibchaweb.chibchaweb.usuario.domain.Cliente;

public class Dominio {

    private Long id;
    private String nombre;
    private String extension;
    private EstadoDominio estado;
    private Cliente propietario;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaVencimiento;

    protected Dominio() {
    }

    public Dominio(Long id, String nombre, String extension, Cliente propietario) {
        validarNombre(nombre);
        validarExtension(extension);
        this.id = id;
        this.nombre = nombre.trim().toLowerCase();
        this.extension = extension.trim().toLowerCase();
        this.propietario = propietario;
        this.estado = EstadoDominio.DISPONIBLE;
        this.fechaRegistro = LocalDateTime.now();
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de dominio no puede estar vacio");
        }
    }

    private void validarExtension(String extension) {
        if (extension == null || extension.isBlank()) {
            throw new IllegalArgumentException("La extension del dominio no puede estar vacia");
        }
        if (!extension.startsWith(".")) {
            throw new IllegalArgumentException("La extension debe comenzar con .");
        }
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getExtension() {
        return extension;
    }

    public String getNombreCompleto() {
        return nombre + extension;
    }

    public EstadoDominio getEstado() {
        return estado;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public boolean estaVencido() {
        return fechaVencimiento != null && LocalDateTime.now().isAfter(fechaVencimiento);
    }

    public boolean estaDisponible() {
        return estado == EstadoDominio.DISPONIBLE;
    }

    public void registrar(Cliente propietario, int anios) {
        if (propietario == null) {
            throw new IllegalArgumentException("El propietario no puede ser nulo");
        }
        if (anios <= 0) {
            throw new IllegalArgumentException("Los anios deben ser positivos");
        }
        if (estado != EstadoDominio.DISPONIBLE) {
            throw new IllegalStateException("El dominio no esta disponible para registro");
        }
        this.propietario = propietario;
        this.estado = EstadoDominio.REGISTRADO;
        this.fechaVencimiento = fechaRegistro.plusYears(anios);
    }

    public void renovar(int anios) {
        if (anios <= 0) {
            throw new IllegalArgumentException("Los anios deben ser positivos");
        }
        if (estado == EstadoDominio.DISPONIBLE) {
            throw new IllegalStateException("No se puede renovar un dominio no registrado");
        }
        if (fechaVencimiento == null) {
            this.fechaVencimiento = LocalDateTime.now().plusYears(anios);
        } else {
            this.fechaVencimiento = fechaVencimiento.plusYears(anios);
        }
    }

    public void transferir(Cliente nuevoPropietario) {
        if (nuevoPropietario == null) {
            throw new IllegalArgumentException("El nuevo propietario no puede ser nulo");
        }
        if (estado == EstadoDominio.DISPONIBLE) {
            throw new IllegalStateException("No se puede transferir un dominio no registrado");
        }
        this.propietario = nuevoPropietario;
    }

    public void expirar() {
        this.estado = EstadoDominio.EXPIRADO;
    }

    public void marcarDisponible() {
        this.estado = EstadoDominio.DISPONIBLE;
        this.propietario = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dominio dominio)) return false;
        return id != null && Objects.equals(id, dominio.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Dominio{id=" + id + ", nombre='" + nombre + extension + "', estado=" + estado + "}";
    }
}
