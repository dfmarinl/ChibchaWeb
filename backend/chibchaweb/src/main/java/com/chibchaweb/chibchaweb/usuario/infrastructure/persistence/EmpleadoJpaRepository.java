package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoJpaRepository extends JpaRepository<EmpleadoJpa, Long> {

    Optional<EmpleadoJpa> findByEmail(String email);
}
