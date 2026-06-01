package com.chibchaweb.chibchaweb.pago.application;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.pago.domain.EstadoSuscripcion;
import com.chibchaweb.chibchaweb.pago.domain.Pago;
import com.chibchaweb.chibchaweb.pago.domain.Periodicidad;
import com.chibchaweb.chibchaweb.pago.domain.Suscripcion;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.SuscripcionResponse;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.SuscripcionDataMapper;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.SuscripcionJpaRepository;
import com.chibchaweb.chibchaweb.plan.domain.HostingPlan;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanDataMapper;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;

@Service
@Transactional
public class SuscripcionService {

    private final SuscripcionDataMapper suscripcionMapper;
    private final SuscripcionJpaRepository suscripcionJpaRepository;
    private final HostingPlanDataMapper planMapper;
    private final ClienteDataMapper clienteMapper;

    public SuscripcionService(SuscripcionDataMapper suscripcionMapper,
                               SuscripcionJpaRepository suscripcionJpaRepository,
                               HostingPlanDataMapper planMapper,
                               ClienteDataMapper clienteMapper) {
        this.suscripcionMapper = suscripcionMapper;
        this.suscripcionJpaRepository = suscripcionJpaRepository;
        this.planMapper = planMapper;
        this.clienteMapper = clienteMapper;
    }

    public Suscripcion buscarPorId(Long id) {
        return suscripcionMapper.findById(id);
    }

    public Suscripcion activar(Long clienteId, Long planId, Periodicidad periodicidad, Pago pago) {
        Cliente cliente = clienteMapper.findById(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        HostingPlan plan = planMapper.findById(planId);
        if (plan == null) {
            throw new IllegalArgumentException("Plan de hosting no encontrado");
        }

        suscripcionJpaRepository.findByClienteIdAndPlanHostingIdAndEstado(clienteId, planId, EstadoSuscripcion.ACTIVA)
                .ifPresent(s -> {
                    throw new IllegalStateException("Ya tienes una suscripción activa para el plan " + plan.getNombre());
                });

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaFin = ahora.plusMonths(periodicidad.getMeses());

        Suscripcion suscripcion = new Suscripcion(null, plan, cliente, periodicidad,
                                                    ahora, fechaFin, EstadoSuscripcion.ACTIVA, pago);
        suscripcionMapper.insert(suscripcion);
        return suscripcion;
    }

    @Transactional(readOnly = true)
    public List<SuscripcionResponse> consultarPorCliente(Long clienteId) {
        return suscripcionJpaRepository.findByClienteIdOrderByFechaInicioDesc(clienteId).stream()
                .map(jpa -> toResponse(suscripcionMapper.toDomain(jpa)))
                .toList();
    }

    public SuscripcionResponse renovar(Long suscripcionId, Periodicidad periodicidad, Pago nuevoPago) {
        Suscripcion suscripcion = suscripcionMapper.findById(suscripcionId);
        if (suscripcion == null) {
            throw new IllegalArgumentException("Suscripción no encontrada");
        }
        suscripcion.renovar(periodicidad, nuevoPago);
        suscripcionMapper.update(suscripcion);
        return toResponse(suscripcion);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void verificarExpiraciones() {
        List<Suscripcion> expiradas = suscripcionJpaRepository
                .findByEstadoAndFechaFinBefore(EstadoSuscripcion.ACTIVA, LocalDateTime.now())
                .stream()
                .map(jpa -> suscripcionMapper.toDomain(jpa))
                .toList();
        for (Suscripcion s : expiradas) {
            s.marcarExpirada();
            suscripcionMapper.update(s);
        }
    }

    private SuscripcionResponse toResponse(Suscripcion s) {
        return new SuscripcionResponse(
            s.getId(),
            s.getPlanHosting().getId(),
            s.getPlanHosting().getNombre(),
            s.getPeriodicidad().name(),
            s.getFechaInicio(),
            s.getFechaFin(),
            s.getEstado().name(),
            s.getPago() != null ? s.getPago().getId() : null
        );
    }
}
