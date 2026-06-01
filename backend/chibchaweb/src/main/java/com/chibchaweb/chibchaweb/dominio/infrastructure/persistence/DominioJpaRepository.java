package com.chibchaweb.chibchaweb.dominio.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DominioJpaRepository extends JpaRepository<DominioJpa, Long> {

    @Transactional
    void deleteByPropietarioId(Long propietarioId);
}
