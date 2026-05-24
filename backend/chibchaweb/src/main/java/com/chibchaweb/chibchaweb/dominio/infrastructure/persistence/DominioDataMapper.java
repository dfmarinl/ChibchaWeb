package com.chibchaweb.chibchaweb.dominio.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.dominio.domain.Dominio;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;

@Component
public class DominioDataMapper implements DataMapper<Dominio, Long> {

    private final DominioJpaRepository repository;
    private final ClienteDataMapper clienteMapper;

    public DominioDataMapper(DominioJpaRepository repository, ClienteDataMapper clienteMapper) {
        this.repository = repository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public void insert(Dominio entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Dominio entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Dominio findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Dominio> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    public DominioJpa toJpa(Dominio domain) {
        if (domain == null) return null;
        DominioJpa jpa = new DominioJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setExtension(domain.getExtension());
        jpa.setEstado(domain.getEstado());
        jpa.setFechaRegistro(domain.getFechaRegistro());
        jpa.setFechaVencimiento(domain.getFechaVencimiento());
        return jpa;
    }

    public Dominio toDomain(DominioJpa jpa) {
        if (jpa == null) return null;
        Dominio dominio = new Dominio(
            jpa.getId(),
            jpa.getNombre(),
            jpa.getExtension(),
            clienteMapper.toDomain(jpa.getPropietario())
        );
        return dominio;
    }
}
