package com.chibchaweb.chibchaweb.dominio.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.chibchaweb.chibchaweb.usuario.domain.Cliente;

public class SitioWeb {

    private Long id;
    private String urlSitio;
    private double espacioUsado;
    private boolean estadoActivo;
    private LocalDateTime fechaCreacion;
    private Cliente propietario;
    private Dominio dominio;
    private Object hostingPlan;

    protected SitioWeb() {
    }

    public SitioWeb(Long id, String urlSitio, Cliente propietario, Dominio dominio) {
        validarUrl(urlSitio);
        if (propietario == null) {
            throw new IllegalArgumentException("El propietario no puede ser nulo");
        }
        if (dominio == null) {
            throw new IllegalArgumentException("El dominio no puede ser nulo");
        }
        this.id = id;
        this.urlSitio = urlSitio.trim().toLowerCase();
        this.propietario = propietario;
        this.dominio = dominio;
        this.estadoActivo = true;
        this.espacioUsado = 0.0;
        this.fechaCreacion = LocalDateTime.now();
    }

    private void validarUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("La URL no puede estar vacia");
        }
    }

    public Long getId() {
        return id;
    }

    public String getUrlSitio() {
        return urlSitio;
    }

    public double getEspacioUsado() {
        return espacioUsado;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public Dominio getDominio() {
        return dominio;
    }

    public Object getHostingPlan() {
        return hostingPlan;
    }

    public void asignarHostingPlan(Object hostingPlan) {
        if (hostingPlan == null) {
            throw new IllegalArgumentException("El plan de hosting no puede ser nulo");
        }
        this.hostingPlan = hostingPlan;
    }

    public void activar() {
        this.estadoActivo = true;
    }

    public void desactivar() {
        this.estadoActivo = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SitioWeb sitioWeb)) return false;
        return id != null && Objects.equals(id, sitioWeb.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SitioWeb{id=" + id + ", url='" + urlSitio + "', activo=" + estadoActivo + "}";
    }
}
