package com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.distribuidor.domain.Distribuidor;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class DistribuidorDataMapper implements DataMapper<Distribuidor, Long> {

    private final DistribuidorJpaRepository repository;

    public DistribuidorDataMapper(DistribuidorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Distribuidor entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Distribuidor entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Distribuidor findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Distribuidor> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    public DistribuidorJpa toJpa(Distribuidor domain) {
        if (domain == null) return null;
        DistribuidorJpa jpa = new DistribuidorJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setEmail(domain.getEmail());
        jpa.setRegion(domain.getRegion());
        jpa.setCodigoDistribuidor(domain.getCodigoDistribuidor());
        jpa.setMaxDominios(domain.getMaxDominios());
        jpa.setNivelDistribuidor(domain.getNivelDistribuidor());
        return jpa;
    }

    public Distribuidor toDomain(DistribuidorJpa jpa) {
        if (jpa == null) return null;
        return new Distribuidor(
                jpa.getId(),
                jpa.getNombre(),
                jpa.getEmail(),
                jpa.getRegion(),
                jpa.getCodigoDistribuidor(),
                jpa.getMaxDominios());
    }
}
