package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostingPlanJpaRepository extends JpaRepository<HostingPlanJpa, Long> {
}
