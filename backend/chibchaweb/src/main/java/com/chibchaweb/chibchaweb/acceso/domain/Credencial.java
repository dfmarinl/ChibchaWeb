package com.chibchaweb.chibchaweb.acceso.domain;

import java.util.Objects;

public class Credencial {

    private static final int MAX_INTENTOS_FALLIDOS = 3;

    private Long id;
    private String email;
    private String hashContrasena;
    private int intentosFallidos;

    public Credencial() {
    }

    public Credencial(Long id, String email, String hashContrasena) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Formato de email invalido");
        }
        if (hashContrasena == null || hashContrasena.isBlank()) {
            throw new IllegalArgumentException("El hash de contrasena no puede estar vacio");
        }
        this.id = id;
        this.email = email.trim().toLowerCase();
        this.hashContrasena = hashContrasena;
        this.intentosFallidos = 0;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getHashContrasena() {
        return hashContrasena;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public boolean verificarContrasena(String rawPassword, PasswordEncoder encoder) {
        if (rawPassword == null || rawPassword.isBlank()) {
            return false;
        }
        return encoder.matches(rawPassword, hashContrasena);
    }

    public void registrarIntentoFallido() {
        intentosFallidos++;
    }

    public void reiniciarIntentos() {
        this.intentosFallidos = 0;
    }

    public boolean estaBloqueada() {
        return intentosFallidos >= MAX_INTENTOS_FALLIDOS;
    }

    public void cambiarEmail(String nuevoEmail) {
        if (nuevoEmail == null || nuevoEmail.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        if (!nuevoEmail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Formato de email invalido");
        }
        this.email = nuevoEmail.trim().toLowerCase();
    }

    public void cambiarContrasena(String nuevoHash) {
        if (nuevoHash == null || nuevoHash.isBlank()) {
            throw new IllegalArgumentException("El hash de contrasena no puede estar vacio");
        }
        this.hashContrasena = nuevoHash;
        this.intentosFallidos = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credencial that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Credencial{id=" + id + ", email='" + email + "', bloqueada=" + estaBloqueada() + "}";
    }

}
