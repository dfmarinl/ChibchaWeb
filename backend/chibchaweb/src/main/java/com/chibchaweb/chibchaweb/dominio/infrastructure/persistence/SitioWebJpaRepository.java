package com.chibchaweb.chibchaweb.dominio.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitioWebJpaRepository extends JpaRepository<SitioWebJpa, Long> {
    List<SitioWebJpa> findByPropietarioId(Long propietarioId);
}
