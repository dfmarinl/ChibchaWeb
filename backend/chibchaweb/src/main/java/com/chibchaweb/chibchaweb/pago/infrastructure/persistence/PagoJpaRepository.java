package com.chibchaweb.chibchaweb.pago.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoJpaRepository extends JpaRepository<PagoJpa, Long> {
}
