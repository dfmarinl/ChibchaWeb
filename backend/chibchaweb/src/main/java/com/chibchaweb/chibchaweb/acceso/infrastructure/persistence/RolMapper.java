package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.acceso.domain.Rol;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class RolMapper implements DataMapper<Rol, Long> {

    private final RolJpaRepository repository;
    private final PermisoMapper permisoMapper;

    public RolMapper(RolJpaRepository repository, PermisoMapper permisoMapper) {
        this.repository = repository;
        this.permisoMapper = permisoMapper;
    }

    @Override
    public void insert(Rol entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Rol entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Rol findById(Long id) {
        return repository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<Rol> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    public RolJpa toJpa(Rol domain) {
        if (domain == null) return null;
        RolJpa jpa = new RolJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        Set<PermisoJpa> permisosJpa = domain.getPermisos().stream()
                .map(permisoMapper::toJpa)
                .collect(Collectors.toSet());
        jpa.setPermisos(permisosJpa);
        return jpa;
    }

    public Rol toDomain(RolJpa jpa) {
        if (jpa == null) return null;
        Set<com.chibchaweb.chibchaweb.acceso.domain.Permiso> permisos =
                jpa.getPermisos().stream()
                        .map(permisoMapper::toDomain)
                        .collect(Collectors.toSet());
        return new Rol(jpa.getId(), jpa.getNombre(), permisos);
    }
}
