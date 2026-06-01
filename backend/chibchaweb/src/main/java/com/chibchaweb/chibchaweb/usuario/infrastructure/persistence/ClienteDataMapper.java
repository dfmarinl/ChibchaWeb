package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;

@Component
public class ClienteDataMapper implements DataMapper<Cliente, Long> {

    private final ClienteJpaRepository repository;

    public ClienteDataMapper(ClienteJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Cliente entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Cliente entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Cliente findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Cliente> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    public ClienteJpa toJpa(Cliente domain) {
        if (domain == null) return null;
        ClienteJpa jpa = new ClienteJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setEmail(domain.getEmail());
        jpa.setTelefono(domain.getTelefono());
        jpa.setFechaRegistro(domain.getFechaRegistro());
        jpa.setDireccion(domain.getDireccion());
        jpa.setDocumentoIdentidad(domain.getDocumentoIdentidad());
        jpa.setRegion(domain.getRegion());
        return jpa;
    }

    public Cliente toDomain(ClienteJpa jpa) {
        if (jpa == null) return null;
        return new Cliente(
            jpa.getId(),
            jpa.getNombre(),
            jpa.getEmail(),
            jpa.getTelefono(),
            jpa.getDireccion(),
            jpa.getDocumentoIdentidad(),
            jpa.getRegion()
        );
    }
}
