package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.domain.Administrador;

@Component
public class AdministradorDataMapper implements DataMapper<Administrador, Long> {

    private final AdministradorJpaRepository repository;

    public AdministradorDataMapper(AdministradorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Administrador entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Administrador entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Administrador findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Administrador> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private AdministradorJpa toJpa(Administrador domain) {
        if (domain == null) return null;
        AdministradorJpa jpa = new AdministradorJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setEmail(domain.getEmail());
        jpa.setTelefono(domain.getTelefono());
        jpa.setFechaRegistro(domain.getFechaRegistro());
        jpa.setNivelAcceso(domain.getNivelAcceso());
        return jpa;
    }

    private Administrador toDomain(AdministradorJpa jpa) {
        if (jpa == null) return null;
        return new Administrador(
            jpa.getId(),
            jpa.getNombre(),
            jpa.getEmail(),
            jpa.getTelefono(),
            jpa.getNivelAcceso()
        );
    }
}
