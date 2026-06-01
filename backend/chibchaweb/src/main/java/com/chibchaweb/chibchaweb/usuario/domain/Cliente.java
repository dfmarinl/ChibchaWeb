package com.chibchaweb.chibchaweb.usuario.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chibchaweb.chibchaweb.dominio.domain.Dominio;
import com.chibchaweb.chibchaweb.dominio.domain.SitioWeb;
import com.chibchaweb.chibchaweb.pago.domain.Pago;
import com.chibchaweb.chibchaweb.pago.domain.TarjetaCredito;
import com.chibchaweb.chibchaweb.soporte.domain.Ticket;

public class Cliente extends Usuario {

    private String direccion;
    private String documentoIdentidad;
    private String region;
    private List<SitioWeb> sitiosWeb;
    private List<TarjetaCredito> tarjetas;
    private List<Ticket> tickets;
    private List<Pago> pagos;
    private List<Dominio> dominios;

    public Cliente() {
    }

    public Cliente(Long id, String nombre, String email, String telefono,
                   String direccion, String documentoIdentidad) {
        super(id, nombre, email, telefono);
        validarDocumentoIdentidad(documentoIdentidad);
        this.direccion = (direccion != null) ? direccion.trim() : null;
        this.documentoIdentidad = documentoIdentidad.trim();
        this.sitiosWeb = new ArrayList<>();
        this.tarjetas = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.pagos = new ArrayList<>();
        this.dominios = new ArrayList<>();
    }

    public Cliente(Long id, String nombre, String email, String telefono,
                   String direccion, String documentoIdentidad, String region) {
        this(id, nombre, email, telefono, direccion, documentoIdentidad);
        this.region = (region != null) ? region.trim() : null;
    }

    private void validarDocumentoIdentidad(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento de identidad no puede estar vacio");
        }
    }

    @Override
    public String getTipoUsuario() {
        return "CLIENTE";
    }

    public String getDireccion() {
        return direccion;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getRegion() {
        return region;
    }

    public List<SitioWeb> getSitiosWeb() {
        return Collections.unmodifiableList(sitiosWeb);
    }

    public List<TarjetaCredito> getTarjetas() {
        return Collections.unmodifiableList(tarjetas);
    }

    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    public List<Pago> getPagos() {
        return Collections.unmodifiableList(pagos);
    }

    public List<Dominio> getDominios() {
        return Collections.unmodifiableList(dominios);
    }

    public void actualizarDireccion(String nuevaDireccion) {
        if (nuevaDireccion == null || nuevaDireccion.isBlank()) {
            throw new IllegalArgumentException("La direccion no puede estar vacia");
        }
        this.direccion = nuevaDireccion.trim();
    }

    public void agregarSitioWeb(SitioWeb sitioWeb) {
        if (sitioWeb == null) {
            throw new IllegalArgumentException("El sitio web no puede ser nulo");
        }
        this.sitiosWeb.add(sitioWeb);
    }

    public void agregarTarjeta(TarjetaCredito tarjeta) {
        if (tarjeta == null) {
            throw new IllegalArgumentException("La tarjeta no puede ser nula");
        }
        this.tarjetas.add(tarjeta);
    }

    public void agregarDominio(Dominio dominio) {
        if (dominio == null) {
            throw new IllegalArgumentException("El dominio no puede ser nulo");
        }
        this.dominios.add(dominio);
    }

    public void agregarTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("El ticket no puede ser nulo");
        }
        this.tickets.add(ticket);
    }

    public void agregarPago(Pago pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser nulo");
        }
        this.pagos.add(pago);
    }
}
