package com.chibchaweb.chibchaweb.pago.domain;

import java.util.Objects;

public class TarjetaCredito {

    private Long id;
    private String titular;
    private String numero;
    private String fechaVencimiento;
    private String cvv;
    private TipoTarjeta tipoTarjeta;

    protected TarjetaCredito() {
    }

    public TarjetaCredito(Long id, String titular, String numero,
                          String fechaVencimiento, String cvv, TipoTarjeta tipoTarjeta) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("El titular no puede estar vacio");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El numero de tarjeta no puede estar vacio");
        }
        if (fechaVencimiento == null || fechaVencimiento.isBlank()) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede estar vacia");
        }
        if (cvv == null || cvv.isBlank()) {
            throw new IllegalArgumentException("El CVV no puede estar vacio");
        }
        if (tipoTarjeta == null) {
            throw new IllegalArgumentException("El tipo de tarjeta no puede ser nulo");
        }
        this.id = id;
        this.titular = titular.trim();
        this.numero = numero.trim();
        this.fechaVencimiento = fechaVencimiento.trim();
        this.cvv = cvv.trim();
        this.tipoTarjeta = tipoTarjeta;
    }

    public Long getId() {
        return id;
    }

    public String getTitular() {
        return titular;
    }

    public String getNumero() {
        return numero;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getCvv() {
        return cvv;
    }

    public TipoTarjeta getTipoTarjeta() {
        return tipoTarjeta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TarjetaCredito that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "TarjetaCredito{id=" + id + ", titular='" + titular + "', tipo=" + tipoTarjeta + "}";
    }
}
