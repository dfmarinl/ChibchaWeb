package com.chibchaweb.chibchaweb.distribuidor.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.chibchaweb.chibchaweb.pago.domain.Comision;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;

public class Distribuidor {

    private Long id;
    private String nombre;
    private String email;
    private String region;
    private String codigoDistribuidor;
    private int maxDominios;
    private NivelDistribuidor nivelDistribuidor;
    private Comision comision;
    private List<Cliente> clientes;

    public Distribuidor() {
    }

    public Distribuidor(Long id, String nombre, String email, String region,
                        String codigoDistribuidor, int maxDominios) {
        validarNombre(nombre);
        validarEmail(email);
        this.id = id;
        this.nombre = nombre.trim();
        this.email = email.trim().toLowerCase();
        this.region = (region != null) ? region.trim() : null;
        this.codigoDistribuidor = codigoDistribuidor;
        this.maxDominios = maxDominios;
        this.nivelDistribuidor = NivelDistribuidor.calcularNivel(maxDominios);
        this.clientes = new ArrayList<>();
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

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getRegion() {
        return region;
    }

    public String getCodigoDistribuidor() {
        return codigoDistribuidor;
    }

    public int getMaxDominios() {
        return maxDominios;
    }

    public NivelDistribuidor getNivelDistribuidor() {
        return nivelDistribuidor;
    }

    public void actualizarMaxDominios(int maxDominios) {
        this.maxDominios = maxDominios;
        this.nivelDistribuidor = NivelDistribuidor.calcularNivel(maxDominios);
    }

    public Comision getComision() {
        return comision;
    }

    public List<Cliente> getClientes() {
        return clientes != null ? Collections.unmodifiableList(clientes) : Collections.emptyList();
    }

    public void asignarComision(Comision comision) {
        if (comision == null) {
            throw new IllegalArgumentException("La comision no puede ser nula");
        }
        this.comision = comision;
    }

    public void agregarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (this.clientes == null) {
            this.clientes = new ArrayList<>();
        }
        this.clientes.add(cliente);
    }

    public void cambiarNivel(NivelDistribuidor nuevoNivel) {
        if (nuevoNivel == null) {
            throw new IllegalArgumentException("El nivel no puede ser nulo");
        }
        this.nivelDistribuidor = nuevoNivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Distribuidor that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Distribuidor{id=" + id + ", nombre='" + nombre + "', codigo='" + codigoDistribuidor + "'}";
    }
}
