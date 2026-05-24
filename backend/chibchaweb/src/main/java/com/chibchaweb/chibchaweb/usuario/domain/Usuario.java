package com.chibchaweb.chibchaweb.usuario.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private LocalDateTime fechaRegistro;

    public Usuario() {
    }

    protected Usuario(Long id, String nombre, String email, String telefono) {
        validarNombre(nombre);
        validarEmail(email);
        this.id = id;
        this.nombre = nombre.trim();
        this.email = email.trim().toLowerCase();
        this.telefono = (telefono != null) ? telefono.trim() : null;
        this.fechaRegistro = LocalDateTime.now();
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Formato de email invalido: " + email);
        }
    }

    public abstract String getTipoUsuario();

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void cambiarEmail(String nuevoEmail) {
        validarEmail(nuevoEmail);
        this.email = nuevoEmail.trim().toLowerCase();
    }

    public void cambiarTelefono(String nuevoTelefono) {
        this.telefono = (nuevoTelefono != null) ? nuevoTelefono.trim() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return id != null && Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", nombre='" + nombre + "', email='" + email + "'}";
    }
}
