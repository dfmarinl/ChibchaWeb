package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.acceso.domain.Sesion;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class SesionMapper implements DataMapper<Sesion, Long> {

    private final SesionJpaRepository repository;

    public SesionMapper(SesionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Sesion entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Sesion entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Sesion findById(Long id) {
        return repository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<Sesion> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    public SesionJpa toJpa(Sesion domain) {
        if (domain == null) return null;
        SesionJpa jpa = new SesionJpa();
        jpa.setId(domain.getId());
        jpa.setCuentaId(domain.getCuentaId());
        jpa.setFechaEmision(domain.getFechaEmision());
        jpa.setFechaExpiracion(domain.getFechaExpiracion());
        jpa.setActiva(domain.isActiva());
        return jpa;
    }

    public Sesion toDomain(SesionJpa jpa) {
        if (jpa == null) return null;
        return new Sesion(
                jpa.getId(),
                jpa.getCuentaId(),
                jpa.getFechaEmision(),
                jpa.getFechaExpiracion(),
                jpa.isActiva());
    }
}
