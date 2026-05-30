package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.pago.domain.Comision;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class ComisionDataMapper implements DataMapper<Comision, Long> {

    private final ComisionJpaRepository repository;

    public ComisionDataMapper(ComisionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Comision entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Comision entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Comision findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Comision> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    public ComisionJpa toJpa(Comision domain) {
        if (domain == null) return null;
        ComisionJpa jpa = new ComisionJpa();
        jpa.setId(domain.getId());
        jpa.setMontoCalculado(domain.getMontoCalculado());
        jpa.setFechaCalculo(domain.getFechaCalculo());
        jpa.setEstrategia(domain.getEstrategia().getClass().getSimpleName());
        return jpa;
    }

    public Comision toDomain(ComisionJpa jpa) {
        if (jpa == null) return null;
        return null;
    }
}
