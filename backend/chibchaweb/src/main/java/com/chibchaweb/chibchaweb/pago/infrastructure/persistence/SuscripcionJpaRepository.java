package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.chibchaweb.chibchaweb.pago.domain.EstadoSuscripcion;

public interface SuscripcionJpaRepository extends JpaRepository<SuscripcionJpa, Long> {

    List<SuscripcionJpa> findByClienteIdOrderByFechaInicioDesc(Long clienteId);

    List<SuscripcionJpa> findByEstadoAndFechaFinBefore(EstadoSuscripcion estado, LocalDateTime fecha);

    Optional<SuscripcionJpa> findByClienteIdAndPlanHostingIdAndEstado(Long clienteId, Long planHostingId, EstadoSuscripcion estado);

    Optional<SuscripcionJpa> findFirstByClienteIdAndEstado(Long clienteId, EstadoSuscripcion estado);

    @Transactional
    void deleteByClienteId(Long clienteId);
}
