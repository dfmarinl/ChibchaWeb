package com.chibchaweb.chibchaweb.pago.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import com.chibchaweb.chibchaweb.usuario.domain.Cliente;

public class Pago {

    private Long id;
    private double monto;
    private String referencia;
    private EstadoPago estado;
    private LocalDateTime fecha;
    private Cliente cliente;
    private TarjetaCredito tarjeta;
    private EstrategiaPago estrategia;
    private Periodicidad periodicidad;

    protected Pago() {
    }

    public Pago(Long id, double monto, Cliente cliente, TarjetaCredito tarjeta, EstrategiaPago estrategia,
                Periodicidad periodicidad) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (tarjeta == null) {
            throw new IllegalArgumentException("La tarjeta no puede ser nula");
        }
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de pago no puede ser nula");
        }
        if (periodicidad == null) {
            throw new IllegalArgumentException("La periodicidad no puede ser nula");
        }
        this.id = id;
        this.monto = monto;
        this.cliente = cliente;
        this.tarjeta = tarjeta;
        this.estrategia = estrategia;
        this.periodicidad = periodicidad;
        this.estado = EstadoPago.PENDIENTE;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public String getReferencia() {
        return referencia;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public TarjetaCredito getTarjeta() {
        return tarjeta;
    }

    public EstrategiaPago getEstrategia() {
        return estrategia;
    }

    public Periodicidad getPeriodicidad() {
        return periodicidad;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEstrategia(EstrategiaPago estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de pago no puede ser nula");
        }
        this.estrategia = estrategia;
    }

    public ResultadoPago ejecutarPago() {
        if (estado != EstadoPago.PENDIENTE) {
            throw new IllegalStateException("El pago ya fue procesado: " + estado);
        }
        ResultadoPago resultado = estrategia.procesarPago(Map.of(), monto);
        if (resultado.isExitoso()) {
            this.estado = EstadoPago.APROBADO;
            this.referencia = resultado.getTransaccionId();
        } else {
            this.estado = EstadoPago.RECHAZADO;
        }
        return resultado;
    }

    public void anular() {
        if (estado == EstadoPago.APROBADO) {
            this.estado = EstadoPago.ANULADO;
        } else {
            throw new IllegalStateException("Solo se pueden anular pagos aprobados");
        }
    }

    public boolean estaAprobado() {
        return estado == EstadoPago.APROBADO;
    }

    public boolean estaPendiente() {
        return estado == EstadoPago.PENDIENTE;
    }

    public static Pago reconstruir(Long id, double monto, Cliente cliente, TarjetaCredito tarjeta,
                                    EstrategiaPago estrategia, EstadoPago estado,
                                    String referencia, LocalDateTime fecha,
                                    Periodicidad periodicidad) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (tarjeta == null) {
            throw new IllegalArgumentException("La tarjeta no puede ser nula");
        }
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de pago no puede ser nula");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (periodicidad == null) {
            throw new IllegalArgumentException("La periodicidad no puede ser nula");
        }
        Pago pago = new Pago();
        pago.id = id;
        pago.monto = monto;
        pago.cliente = cliente;
        pago.tarjeta = tarjeta;
        pago.estrategia = estrategia;
        pago.estado = estado;
        pago.referencia = referencia;
        pago.fecha = fecha;
        pago.periodicidad = periodicidad;
        return pago;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pago pago)) return false;
        return id != null && Objects.equals(id, pago.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Pago{id=" + id + ", monto=" + monto + ", estado=" + estado + ", periodicidad=" + periodicidad + "}";
    }
}
