package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredencialJpaRepository extends JpaRepository<CredencialJpa, Long> {

    Optional<CredencialJpa> findByEmail(String email);
}
