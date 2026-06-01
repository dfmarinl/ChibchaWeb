package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionJpaRepository extends JpaRepository<SesionJpa, Long> {

    List<SesionJpa> findByCuentaIdAndActivaTrue(Long cuentaId);
}
