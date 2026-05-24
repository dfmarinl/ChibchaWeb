package com.chibchaweb.chibchaweb.dominio.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DominioJpaRepository extends JpaRepository<DominioJpa, Long> {
}
