package com.chibchaweb.chibchaweb.acceso.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Rol {

    private Long id;
    private NombreRol nombre;
    private Set<Permiso> permisos;

    public Rol() {
    }

    public Rol(Long id, NombreRol nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre del rol no puede ser nulo");
        }
        this.id = id;
        this.nombre = nombre;
        this.permisos = new HashSet<>();
    }

    public Rol(Long id, NombreRol nombre, Set<Permiso> permisos) {
        this(id, nombre);
        if (permisos != null) {
            this.permisos.addAll(permisos);
        }
    }

    public Long getId() {
        return id;
    }

    public NombreRol getNombre() {
        return nombre;
    }

    public Set<Permiso> getPermisos() {
        return Collections.unmodifiableSet(permisos);
    }

    public boolean esAdministrador() {
        return nombre == NombreRol.ADMINISTRADOR;
    }

    public boolean esCliente() {
        return nombre == NombreRol.CLIENTE;
    }

    public boolean esEmpleado() {
        return nombre == NombreRol.EMPLEADO;
    }

    public boolean tienePermiso(Permiso permiso) {
        if (permiso == null) {
            return false;
        }
        return permisos.contains(permiso);
    }

    public boolean tienePermiso(String nombrePermiso) {
        if (nombrePermiso == null || nombrePermiso.isBlank()) {
            return false;
        }
        return permisos.stream()
                .anyMatch(p -> p.getNombre().equals(nombrePermiso.trim().toUpperCase()));
    }

    public void agregarPermiso(Permiso permiso) {
        if (permiso == null) {
            throw new IllegalArgumentException("El permiso no puede ser nulo");
        }
        this.permisos.add(permiso);
    }

    public void removerPermiso(Permiso permiso) {
        this.permisos.remove(permiso);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol rol)) return false;
        return id != null && Objects.equals(id, rol.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Rol{id=" + id + ", nombre=" + nombre + ", permisos=" + permisos.size() + "}";
    }

}
