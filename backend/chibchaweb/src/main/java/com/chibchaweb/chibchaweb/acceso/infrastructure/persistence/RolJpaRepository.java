package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;

@Repository
public interface RolJpaRepository extends JpaRepository<RolJpa, Long> {

    Optional<RolJpa> findByNombre(NombreRol nombre);
}
