package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaAccesoJpaRepository extends JpaRepository<CuentaAccesoJpa, Long> {

    Optional<CuentaAccesoJpa> findByCredencialEmail(String email);
}
