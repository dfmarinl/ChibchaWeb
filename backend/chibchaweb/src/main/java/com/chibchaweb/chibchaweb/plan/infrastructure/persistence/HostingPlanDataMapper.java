package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.plan.domain.HostingPlan;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class HostingPlanDataMapper implements DataMapper<HostingPlan, Long> {

    private final HostingPlanJpaRepository repository;

    public HostingPlanDataMapper(HostingPlanJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(HostingPlan entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(HostingPlan entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public HostingPlan findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<HostingPlan> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    HostingPlanJpa toJpa(HostingPlan domain) {
        if (domain == null) return null;
        HostingPlanJpa jpa = new HostingPlanJpa() {};
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setPrecioMensual(domain.getPrecioMensual());
        jpa.setEspacioDisco(domain.getEspacioDisco());
        jpa.setAnchoBanda(domain.getAnchoBanda());
        jpa.setCuentasEmail(domain.getCuentasEmail());
        return jpa;
    }

    private HostingPlan toDomain(HostingPlanJpa jpa) {
        if (jpa == null) return null;
        return null;
    }
}
