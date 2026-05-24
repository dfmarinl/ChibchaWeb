package com.chibchaweb.chibchaweb.acceso.domain;

import java.util.Objects;

public class Permiso {

    private Long id;
    private String nombre;

    protected Permiso() {
    }

    public Permiso(Long id, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del permiso no puede estar vacio");
        }
        this.id = id;
        this.nombre = nombre.trim().toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permiso permiso)) return false;
        return id != null && Objects.equals(id, permiso.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Permiso{id=" + id + ", nombre='" + nombre + "'}";
    }

}
