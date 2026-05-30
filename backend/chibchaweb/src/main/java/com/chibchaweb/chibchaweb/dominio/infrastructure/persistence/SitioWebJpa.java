package com.chibchaweb.chibchaweb.dominio.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpa;

@Entity
@Table(name = "sitio_web")
public class SitioWebJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_sitio", nullable = false)
    private String urlSitio;

    @Column(name = "espacio_usado")
    private double espacioUsado;

    @Column(name = "estado_activo")
    private boolean estadoActivo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "propietario_id", nullable = false)
    private ClienteJpa propietario;

    @ManyToOne
    @JoinColumn(name = "dominio_id")
    private DominioJpa dominio;

    @Column(name = "hosting_plan_id")
    private Long hostingPlanId;

    public SitioWebJpa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlSitio() {
        return urlSitio;
    }

    public void setUrlSitio(String urlSitio) {
        this.urlSitio = urlSitio;
    }

    public double getEspacioUsado() {
        return espacioUsado;
    }

    public void setEspacioUsado(double espacioUsado) {
        this.espacioUsado = espacioUsado;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ClienteJpa getPropietario() {
        return propietario;
    }

    public void setPropietario(ClienteJpa propietario) {
        this.propietario = propietario;
    }

    public DominioJpa getDominio() {
        return dominio;
    }

    public void setDominio(DominioJpa dominio) {
        this.dominio = dominio;
    }

    public Long getHostingPlanId() {
        return hostingPlanId;
    }

    public void setHostingPlanId(Long hostingPlanId) {
        this.hostingPlanId = hostingPlanId;
    }
}
