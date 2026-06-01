package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteJpa, Long> {

    Optional<ClienteJpa> findByEmail(String email);

    Optional<ClienteJpa> findByDocumentoIdentidad(String documentoIdentidad);
}
