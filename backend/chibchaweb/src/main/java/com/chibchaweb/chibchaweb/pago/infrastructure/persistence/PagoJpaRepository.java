package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PagoJpaRepository extends JpaRepository<PagoJpa, Long> {
    List<PagoJpa> findByClienteIdOrderByFechaDesc(Long clienteId);
    List<PagoJpa> findAllByOrderByFechaDesc();

    @Transactional
    void deleteByClienteId(Long clienteId);
}
