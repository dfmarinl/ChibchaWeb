package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.pago.domain.TarjetaCredito;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class TarjetaCreditoDataMapper implements DataMapper<TarjetaCredito, Long> {

    private final TarjetaCreditoJpaRepository repository;

    public TarjetaCreditoDataMapper(TarjetaCreditoJpaRepository repository) {
        this.repository = repository;
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
        return jpa;
    }

    private TarjetaCredito toDomain(TarjetaCreditoJpa jpa) {
        if (jpa == null) return null;
        return new TarjetaCredito(
            jpa.getId(),
            jpa.getTitular(),
            jpa.getNumero(),
            jpa.getFechaVencimiento(),
            jpa.getCvv(),
            jpa.getTipoTarjeta()
        );
    }
}
