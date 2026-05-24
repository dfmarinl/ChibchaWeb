package com.chibchaweb.chibchaweb.soporte.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.soporte.domain.Ticket;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;

@Component
public class TicketDataMapper implements DataMapper<Ticket, Long> {

    private final TicketJpaRepository repository;
    private final ClienteDataMapper clienteMapper;

    public TicketDataMapper(TicketJpaRepository repository, ClienteDataMapper clienteMapper) {
        this.repository = repository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public void insert(Ticket entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Ticket entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Ticket findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Ticket> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private TicketJpa toJpa(Ticket domain) {
        if (domain == null) return null;
        TicketJpa jpa = new TicketJpa();
        jpa.setId(domain.getId());
        jpa.setTitulo(domain.getTitulo());
        jpa.setDescripcion(domain.getDescripcion());
        jpa.setNivel(domain.getNivel());
        jpa.setPrioridad(domain.getPrioridad());
        jpa.setEstado(domain.getEstado());
        jpa.setFechaCreacion(domain.getFechaCreacion());
        jpa.setFechaResolucion(domain.getFechaResolucion());
        jpa.setHistorial(domain.getHistorial());
        return jpa;
    }

    private Ticket toDomain(TicketJpa jpa) {
        if (jpa == null) return null;
        return null;
    }
}
