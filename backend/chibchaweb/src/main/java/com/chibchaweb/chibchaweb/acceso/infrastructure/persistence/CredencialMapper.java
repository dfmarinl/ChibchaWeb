package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.acceso.domain.Credencial;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class CredencialMapper implements DataMapper<Credencial, Long> {

    private final CredencialJpaRepository repository;

    public CredencialMapper(CredencialJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Credencial entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Credencial entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Credencial findById(Long id) {
        return repository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<Credencial> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    public CredencialJpa toJpa(Credencial domain) {
        if (domain == null) return null;
        CredencialJpa jpa = new CredencialJpa();
        jpa.setId(domain.getId());
        jpa.setEmail(domain.getEmail());
        jpa.setHashContrasena(domain.getHashContrasena());
        jpa.setIntentosFallidos(domain.getIntentosFallidos());
        return jpa;
    }

    public Credencial toDomain(CredencialJpa jpa) {
        if (jpa == null) return null;
        return new Credencial(jpa.getId(), jpa.getEmail(), jpa.getHashContrasena());
    }
}
