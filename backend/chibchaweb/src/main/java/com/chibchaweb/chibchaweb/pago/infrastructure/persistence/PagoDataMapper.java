package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.pago.domain.Pago;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;

@Component
public class PagoDataMapper implements DataMapper<Pago, Long> {

    private final PagoJpaRepository repository;
    private final ClienteDataMapper clienteMapper;

    public PagoDataMapper(PagoJpaRepository repository, ClienteDataMapper clienteMapper) {
        this.repository = repository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public void insert(Pago entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Pago entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Pago findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Pago> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private PagoJpa toJpa(Pago domain) {
        if (domain == null) return null;
        PagoJpa jpa = new PagoJpa();
        jpa.setId(domain.getId());
        jpa.setMonto(domain.getMonto());
        jpa.setReferencia(domain.getReferencia());
        jpa.setEstado(domain.getEstado());
        jpa.setFecha(domain.getFecha());
        return jpa;
    }

    private Pago toDomain(PagoJpa jpa) {
        if (jpa == null) return null;
        return null;
    }
}
