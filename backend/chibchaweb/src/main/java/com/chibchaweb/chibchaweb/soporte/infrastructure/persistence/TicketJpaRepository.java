package com.chibchaweb.chibchaweb.soporte.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TicketJpaRepository extends JpaRepository<TicketJpa, Long> {

    @Transactional
    void deleteByClienteId(Long clienteId);
}
