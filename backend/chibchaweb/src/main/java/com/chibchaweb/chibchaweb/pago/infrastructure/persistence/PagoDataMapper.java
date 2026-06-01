package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.pago.domain.EstrategiaPago;
import com.chibchaweb.chibchaweb.pago.domain.Pago;
import com.chibchaweb.chibchaweb.pago.domain.PagoDiners;
import com.chibchaweb.chibchaweb.pago.domain.PagoMastercard;
import com.chibchaweb.chibchaweb.pago.domain.PagoVisa;
import com.chibchaweb.chibchaweb.pago.domain.TarjetaCredito;
import com.chibchaweb.chibchaweb.pago.domain.TipoTarjeta;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpa;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpaRepository;

@Component
public class PagoDataMapper implements DataMapper<Pago, Long> {

    private final PagoJpaRepository repository;
    private final ClienteDataMapper clienteMapper;
    private final TarjetaCreditoDataMapper tarjetaMapper;
    private final ClienteJpaRepository clienteJpaRepository;
    private final TarjetaCreditoJpaRepository tarjetaJpaRepository;

    public PagoDataMapper(PagoJpaRepository repository, ClienteDataMapper clienteMapper,
                          TarjetaCreditoDataMapper tarjetaMapper,
                          ClienteJpaRepository clienteJpaRepository,
                          TarjetaCreditoJpaRepository tarjetaJpaRepository) {
        this.repository = repository;
        this.clienteMapper = clienteMapper;
        this.tarjetaMapper = tarjetaMapper;
        this.clienteJpaRepository = clienteJpaRepository;
        this.tarjetaJpaRepository = tarjetaJpaRepository;
    }

    @Override
    public void insert(Pago entidad) {
        PagoJpa saved = repository.save(toJpa(entidad));
        entidad.setId(saved.getId());
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

    public PagoJpa toJpa(Pago domain) {
        if (domain == null) return null;
        PagoJpa jpa = new PagoJpa();
        jpa.setId(domain.getId());
        jpa.setMonto(domain.getMonto());
        jpa.setReferencia(domain.getReferencia());
        jpa.setEstado(domain.getEstado());
        jpa.setFecha(domain.getFecha());
        jpa.setPeriodicidad(domain.getPeriodicidad());
        if (domain.getCliente() != null) {
            ClienteJpa clienteJpa = clienteJpaRepository.findById(domain.getCliente().getId()).orElse(null);
            jpa.setCliente(clienteJpa);
        }
        if (domain.getTarjeta() != null) {
            TarjetaCreditoJpa tarjetaJpa = tarjetaJpaRepository.findById(domain.getTarjeta().getId()).orElse(null);
            jpa.setTarjeta(tarjetaJpa);
        }
        return jpa;
    }

    public Pago toDomain(PagoJpa jpa) {
        if (jpa == null) return null;
        Cliente cliente = clienteMapper.toDomain(jpa.getCliente());
        TarjetaCredito tarjeta = tarjetaMapper.toDomain(jpa.getTarjeta());
        EstrategiaPago estrategia = crearEstrategia(tarjeta != null ? tarjeta.getTipoTarjeta() : null);
        return Pago.reconstruir(jpa.getId(), jpa.getMonto(), cliente, tarjeta, estrategia,
                                 jpa.getEstado(), jpa.getReferencia(), jpa.getFecha(),
                                 jpa.getPeriodicidad());
    }

    private EstrategiaPago crearEstrategia(TipoTarjeta tipo) {
        if (tipo == null) return new PagoVisa();
        return switch (tipo) {
            case VISA -> new PagoVisa();
            case MASTERCARD -> new PagoMastercard();
            case DINERS -> new PagoDiners();
        };
    }
}
