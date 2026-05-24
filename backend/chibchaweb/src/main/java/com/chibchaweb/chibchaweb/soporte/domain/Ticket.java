package com.chibchaweb.chibchaweb.soporte.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.chibchaweb.chibchaweb.usuario.domain.Cliente;

public class Ticket {

    private Long id;
    private String titulo;
    private String descripcion;
    private int nivel;
    private PrioridadTicket prioridad;
    private EstadoTicket estado;
    private Cliente cliente;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion;
    private List<String> historial;

    protected Ticket() {
    }

    public Ticket(Long id, String titulo, String descripcion, PrioridadTicket prioridad, Cliente cliente) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripcion no puede estar vacia");
        }
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.descripcion = descripcion.trim();
        this.prioridad = prioridad;
        this.cliente = cliente;
        this.estado = EstadoTicket.ABIERTO;
        this.nivel = 1;
        this.fechaCreacion = LocalDateTime.now();
        this.historial = new ArrayList<>();
        this.historial.add("Ticket creado - Prioridad: " + prioridad);
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNivel() {
        return nivel;
    }

    public PrioridadTicket getPrioridad() {
        return prioridad;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public List<String> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public void escalar() {
        if (estado == EstadoTicket.CERRADO) {
            throw new IllegalStateException("No se puede escalar un ticket cerrado");
        }
        this.nivel++;
        this.estado = EstadoTicket.ESCALADO;
        this.historial.add("Ticket escalado a nivel " + nivel);
    }

    public void procesar() {
        if (estado == EstadoTicket.CERRADO) {
            throw new IllegalStateException("No se puede procesar un ticket cerrado");
        }
        this.estado = EstadoTicket.EN_PROCESO;
        this.historial.add("Ticket en proceso");
    }

    public void resolver() {
        this.estado = EstadoTicket.RESUELTO;
        this.fechaResolucion = LocalDateTime.now();
        this.historial.add("Ticket resuelto");
    }

    public void cerrar() {
        this.estado = EstadoTicket.CERRADO;
        if (this.fechaResolucion == null) {
            this.fechaResolucion = LocalDateTime.now();
        }
        this.historial.add("Ticket cerrado");
    }

    public void reabrir(String motivo) {
        if (estado != EstadoTicket.CERRADO) {
            throw new IllegalStateException("Solo se pueden reabrir tickets cerrados");
        }
        this.estado = EstadoTicket.ABIERTO;
        this.fechaResolucion = null;
        this.historial.add("Ticket reabierto: " + motivo);
    }

    public void agregarComentario(String comentario) {
        if (comentario == null || comentario.isBlank()) {
            throw new IllegalArgumentException("El comentario no puede estar vacio");
        }
        this.historial.add(comentario.trim());
    }

    public boolean estaAbierto() {
        return estado == EstadoTicket.ABIERTO || estado == EstadoTicket.EN_PROCESO;
    }

    public boolean estaResuelto() {
        return estado == EstadoTicket.RESUELTO || estado == EstadoTicket.CERRADO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;
        return id != null && Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Ticket{id=" + id + ", titulo='" + titulo + "', estado=" + estado + "}";
    }
}
