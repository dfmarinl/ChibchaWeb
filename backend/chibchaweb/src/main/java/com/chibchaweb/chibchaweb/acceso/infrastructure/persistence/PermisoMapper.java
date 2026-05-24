package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.acceso.domain.Permiso;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class PermisoMapper implements DataMapper<Permiso, Long> {

    private final PermisoJpaRepository repository;

    public PermisoMapper(PermisoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Permiso entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Permiso entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Permiso findById(Long id) {
        return repository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<Permiso> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    public PermisoJpa toJpa(Permiso domain) {
        if (domain == null) return null;
        PermisoJpa jpa = new PermisoJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        return jpa;
    }

    public Permiso toDomain(PermisoJpa jpa) {
        if (jpa == null) return null;
        return new Permiso(jpa.getId(), jpa.getNombre());
    }
}
