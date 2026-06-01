package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import com.chibchaweb.chibchaweb.pago.domain.EstadoSuscripcion;
import com.chibchaweb.chibchaweb.pago.domain.Periodicidad;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanJpa;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpa;

@Entity
@Table(name = "suscripcion")
public class SuscripcionJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private HostingPlanJpa planHosting;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteJpa cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Periodicidad periodicidad;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSuscripcion estado;

    @ManyToOne
    @JoinColumn(name = "pago_id")
    private PagoJpa pago;

    protected SuscripcionJpa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HostingPlanJpa getPlanHosting() {
        return planHosting;
    }

    public void setPlanHosting(HostingPlanJpa planHosting) {
        this.planHosting = planHosting;
    }

    public ClienteJpa getCliente() {
        return cliente;
    }

    public void setCliente(ClienteJpa cliente) {
        this.cliente = cliente;
    }

    public Periodicidad getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(Periodicidad periodicidad) {
        this.periodicidad = periodicidad;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoSuscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoSuscripcion estado) {
        this.estado = estado;
    }

    public PagoJpa getPago() {
        return pago;
    }

    public void setPago(PagoJpa pago) {
        this.pago = pago;
    }
}
