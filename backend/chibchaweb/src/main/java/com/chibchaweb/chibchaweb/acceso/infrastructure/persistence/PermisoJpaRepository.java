package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoJpaRepository extends JpaRepository<PermisoJpa, Long> {
}
