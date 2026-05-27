package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarjetaCreditoJpaRepository extends JpaRepository<TarjetaCreditoJpa, Long> {
    List<TarjetaCreditoJpa> findByClienteId(Long clienteId);
}
