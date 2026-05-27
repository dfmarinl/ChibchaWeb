package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.pago.domain.TarjetaCredito;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpa;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpaRepository;

@Component
public class TarjetaCreditoDataMapper implements DataMapper<TarjetaCredito, Long> {

    private final TarjetaCreditoJpaRepository repository;
    private final ClienteJpaRepository clienteRepository;

    public TarjetaCreditoDataMapper(TarjetaCreditoJpaRepository repository,
                                    ClienteJpaRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void insert(TarjetaCredito entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(TarjetaCredito entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public TarjetaCredito findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<TarjetaCredito> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private TarjetaCreditoJpa toJpa(TarjetaCredito domain) {
        if (domain == null) return null;
        TarjetaCreditoJpa jpa = new TarjetaCreditoJpa();
        jpa.setId(domain.getId());
        jpa.setTitular(domain.getTitular());
        jpa.setNumero(domain.getNumero());
        jpa.setFechaVencimiento(domain.getFechaVencimiento());
        jpa.setCvv(domain.getCvv());
        jpa.setTipoTarjeta(domain.getTipoTarjeta());
        if (domain.getClienteId() != null) {
            ClienteJpa cliente = clienteRepository.findById(domain.getClienteId()).orElse(null);
            jpa.setCliente(cliente);
        }
        return jpa;
    }

    private TarjetaCredito toDomain(TarjetaCreditoJpa jpa) {
        if (jpa == null) return null;
        Long clienteId = jpa.getCliente() != null ? jpa.getCliente().getId() : null;
        return new TarjetaCredito(
            jpa.getId(),
            jpa.getTitular(),
            jpa.getNumero(),
            jpa.getFechaVencimiento(),
            jpa.getCvv(),
            jpa.getTipoTarjeta(),
            clienteId
        );
    }
}
