package com.chibchaweb.chibchaweb.dominio.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.dominio.domain.SitioWeb;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.SuscripcionDataMapper;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.SuscripcionJpaRepository;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;

@Component
public class SitioWebDataMapper implements DataMapper<SitioWeb, Long> {

    private final SitioWebJpaRepository repository;
    private final ClienteDataMapper clienteMapper;
    private final DominioDataMapper dominioMapper;
    private final SuscripcionDataMapper suscripcionMapper;
    private final SuscripcionJpaRepository suscripcionJpaRepository;

    public SitioWebDataMapper(SitioWebJpaRepository repository,
                              ClienteDataMapper clienteMapper,
                              DominioDataMapper dominioMapper,
                              SuscripcionDataMapper suscripcionMapper,
                              SuscripcionJpaRepository suscripcionJpaRepository) {
        this.repository = repository;
        this.clienteMapper = clienteMapper;
        this.dominioMapper = dominioMapper;
        this.suscripcionMapper = suscripcionMapper;
        this.suscripcionJpaRepository = suscripcionJpaRepository;
    }

    @Override
    public void insert(SitioWeb entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(SitioWeb entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public SitioWeb findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<SitioWeb> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    public SitioWebJpa toJpa(SitioWeb domain) {
        if (domain == null) return null;
        SitioWebJpa jpa = new SitioWebJpa();
        jpa.setId(domain.getId());
        jpa.setUrlSitio(domain.getUrlSitio());
        jpa.setEspacioUsado(domain.getEspacioUsado());
        jpa.setEstadoActivo(domain.isEstadoActivo());
        jpa.setFechaCreacion(domain.getFechaCreacion());
        if (domain.getPropietario() != null) {
            jpa.setPropietario(clienteMapper.toJpa(domain.getPropietario()));
        }
        if (domain.getDominio() != null) {
            jpa.setDominio(dominioMapper.toJpa(domain.getDominio()));
        }
        if (domain.getSuscripcion() != null && domain.getSuscripcion().getId() != null) {
            suscripcionJpaRepository.findById(domain.getSuscripcion().getId())
                    .ifPresent(jpa::setSuscripcion);
        }
        return jpa;
    }

    public SitioWeb toDomain(SitioWebJpa jpa) {
        if (jpa == null) return null;
        SitioWeb sitioWeb = new SitioWeb(
            jpa.getId(),
            jpa.getUrlSitio(),
            clienteMapper.toDomain(jpa.getPropietario())
        );
        if (jpa.getDominio() != null) {
            sitioWeb = new SitioWeb(
                jpa.getId(),
                jpa.getUrlSitio(),
                clienteMapper.toDomain(jpa.getPropietario()),
                dominioMapper.toDomain(jpa.getDominio())
            );
        }
        if (jpa.getSuscripcion() != null) {
            sitioWeb.asignarSuscripcion(suscripcionMapper.toDomain(jpa.getSuscripcion()));
        }
        return sitioWeb;
    }
}
