package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.pago.domain.Pago;
import com.chibchaweb.chibchaweb.pago.domain.Suscripcion;
import com.chibchaweb.chibchaweb.plan.domain.HostingPlan;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanDataMapper;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanJpa;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanJpaRepository;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpa;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpaRepository;

import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.PagoJpa;

@Component
public class SuscripcionDataMapper implements DataMapper<Suscripcion, Long> {

    private final SuscripcionJpaRepository repository;
    private final ClienteDataMapper clienteMapper;
    private final ClienteJpaRepository clienteJpaRepository;
    private final HostingPlanDataMapper planMapper;
    private final HostingPlanJpaRepository planJpaRepository;
    private final PagoDataMapper pagoMapper;
    private final PagoJpaRepository pagoJpaRepository;

    public SuscripcionDataMapper(SuscripcionJpaRepository repository, ClienteDataMapper clienteMapper,
                                  ClienteJpaRepository clienteJpaRepository,
                                  HostingPlanDataMapper planMapper,
                                  HostingPlanJpaRepository planJpaRepository,
                                  PagoDataMapper pagoMapper,
                                  PagoJpaRepository pagoJpaRepository) {
        this.repository = repository;
        this.clienteMapper = clienteMapper;
        this.clienteJpaRepository = clienteJpaRepository;
        this.planMapper = planMapper;
        this.planJpaRepository = planJpaRepository;
        this.pagoMapper = pagoMapper;
        this.pagoJpaRepository = pagoJpaRepository;
    }

    @Override
    public void insert(Suscripcion entidad) {
        SuscripcionJpa saved = repository.save(toJpa(entidad));
        entidad.setId(saved.getId());
    }

    @Override
    public void update(Suscripcion entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Suscripcion findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Suscripcion> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    public SuscripcionJpa toJpa(Suscripcion domain) {
        if (domain == null) return null;
        SuscripcionJpa jpa = new SuscripcionJpa();
        jpa.setId(domain.getId());
        if (domain.getPlanHosting() != null && domain.getPlanHosting().getId() != null) {
            HostingPlanJpa planJpa = planJpaRepository.findById(domain.getPlanHosting().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Plan de hosting no encontrado: " + domain.getPlanHosting().getId()));
            jpa.setPlanHosting(planJpa);
        }
        if (domain.getCliente() != null && domain.getCliente().getId() != null) {
            ClienteJpa clienteJpa = clienteJpaRepository.findById(domain.getCliente().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente no encontrado: " + domain.getCliente().getId()));
            jpa.setCliente(clienteJpa);
        }
        jpa.setPeriodicidad(domain.getPeriodicidad());
        jpa.setFechaInicio(domain.getFechaInicio());
        jpa.setFechaFin(domain.getFechaFin());
        jpa.setEstado(domain.getEstado());
        if (domain.getPago() != null && domain.getPago().getId() != null) {
            PagoJpa pagoJpa = pagoJpaRepository.findById(domain.getPago().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Pago no encontrado: " + domain.getPago().getId()));
            jpa.setPago(pagoJpa);
        }
        return jpa;
    }

    public Suscripcion toDomain(SuscripcionJpa jpa) {
        if (jpa == null) return null;
        Cliente cliente = clienteMapper.toDomain(jpa.getCliente());
        HostingPlan plan = planMapper.toDomain(jpa.getPlanHosting());
        Pago pago = jpa.getPago() != null ? pagoMapper.toDomain(jpa.getPago()) : null;
        return Suscripcion.reconstruir(
            jpa.getId(), plan, cliente, jpa.getPeriodicidad(),
            jpa.getFechaInicio(), jpa.getFechaFin(), jpa.getEstado(), pago
        );
    }
}
