package com.chibchaweb.chibchaweb.pago.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.chibchaweb.chibchaweb.plan.domain.HostingPlan;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;

public class Suscripcion {

    private Long id;
    private HostingPlan planHosting;
    private Cliente cliente;
    private Periodicidad periodicidad;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoSuscripcion estado;
    private Pago pago;

    protected Suscripcion() {
    }

    public Suscripcion(Long id, HostingPlan planHosting, Cliente cliente, Periodicidad periodicidad,
                       LocalDateTime fechaInicio, LocalDateTime fechaFin, EstadoSuscripcion estado, Pago pago) {
        if (planHosting == null) {
            throw new IllegalArgumentException("El plan de hosting no puede ser nulo");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (periodicidad == null) {
            throw new IllegalArgumentException("La periodicidad no puede ser nula");
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("La fecha de fin no puede ser nula");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.id = id;
        this.planHosting = planHosting;
        this.cliente = cliente;
        this.periodicidad = periodicidad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.pago = pago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HostingPlan getPlanHosting() {
        return planHosting;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Periodicidad getPeriodicidad() {
        return periodicidad;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public EstadoSuscripcion getEstado() {
        return estado;
    }

    public Pago getPago() {
        return pago;
    }

    public boolean estaActiva() {
        return estado == EstadoSuscripcion.ACTIVA;
    }

    public boolean estaExpirada() {
        return estado == EstadoSuscripcion.EXPIRADA;
    }

    public void renovar(Periodicidad nuevaPeriodicidad, Pago nuevoPago) {
        if (!estaActiva() && !estaExpirada()) {
            throw new IllegalStateException("Solo se pueden renovar suscripciones activas o expiradas");
        }
        if (nuevoPago == null) {
            throw new IllegalArgumentException("El pago de renovación no puede ser nulo");
        }
        LocalDateTime nuevaFechaFin;
        if (this.fechaFin.isAfter(LocalDateTime.now())) {
            nuevaFechaFin = this.fechaFin.plusMonths(nuevaPeriodicidad.getMeses());
        } else {
            nuevaFechaFin = LocalDateTime.now().plusMonths(nuevaPeriodicidad.getMeses());
        }
        this.fechaFin = nuevaFechaFin;
        this.periodicidad = nuevaPeriodicidad;
        this.estado = EstadoSuscripcion.ACTIVA;
        this.pago = nuevoPago;
    }

    public void marcarExpirada() {
        if (estado == EstadoSuscripcion.ACTIVA) {
            this.estado = EstadoSuscripcion.EXPIRADA;
        }
    }

    public void cancelar() {
        if (estado != EstadoSuscripcion.CANCELADA) {
            this.estado = EstadoSuscripcion.CANCELADA;
        }
    }

    public static Suscripcion reconstruir(Long id, HostingPlan planHosting, Cliente cliente,
                                           Periodicidad periodicidad, LocalDateTime fechaInicio,
                                           LocalDateTime fechaFin, EstadoSuscripcion estado, Pago pago) {
        if (planHosting == null) {
            throw new IllegalArgumentException("El plan de hosting no puede ser nulo");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (periodicidad == null) {
            throw new IllegalArgumentException("La periodicidad no puede ser nula");
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("La fecha de fin no puede ser nula");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        Suscripcion s = new Suscripcion();
        s.id = id;
        s.planHosting = planHosting;
        s.cliente = cliente;
        s.periodicidad = periodicidad;
        s.fechaInicio = fechaInicio;
        s.fechaFin = fechaFin;
        s.estado = estado;
        s.pago = pago;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Suscripcion that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Suscripcion{id=" + id + ", plan=" + planHosting.getNombre() + ", estado=" + estado + "}";
    }
}
