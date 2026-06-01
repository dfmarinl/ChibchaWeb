package com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistribuidorJpaRepository extends JpaRepository<DistribuidorJpa, Long> {

    Optional<DistribuidorJpa> findByEmail(String email);

    Optional<DistribuidorJpa> findByNombre(String nombre);
}
