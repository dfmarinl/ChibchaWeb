package com.chibchaweb.chibchaweb.plan.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.plan.domain.HostingPlan;
import com.chibchaweb.chibchaweb.plan.domain.PlanOroUnix;
import com.chibchaweb.chibchaweb.plan.domain.PlanOroWindows;
import com.chibchaweb.chibchaweb.plan.domain.PlanPlataUnix;
import com.chibchaweb.chibchaweb.plan.domain.PlanPlataWindows;
import com.chibchaweb.chibchaweb.plan.domain.PlanPlatinoUnix;
import com.chibchaweb.chibchaweb.plan.domain.PlanPlatinoWindows;
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

    public HostingPlanJpa toJpa(HostingPlan domain) {
        if (domain == null) return null;

        if (domain instanceof PlanPlatinoUnix p) {
            PlanPlatinoUnixJpa jpa = new PlanPlatinoUnixJpa();
            jpa.setId(p.getId());
            jpa.setNombre(p.getNombre());
            jpa.setPrecioMensual(p.getPrecioMensual());
            jpa.setEspacioDisco(p.getEspacioDisco());
            jpa.setAnchoBanda(p.getAnchoBanda());
            jpa.setCuentasEmail(p.getCuentasEmail());
            jpa.setMysqlIncluido(p.isMysqlIncluido());
            jpa.setPhpVersion(p.getPhpVersion());
            return jpa;
        }
        if (domain instanceof PlanPlatinoWindows p) {
            PlanPlatinoWindowsJpa jpa = new PlanPlatinoWindowsJpa();
            jpa.setId(p.getId());
            jpa.setNombre(p.getNombre());
            jpa.setPrecioMensual(p.getPrecioMensual());
            jpa.setEspacioDisco(p.getEspacioDisco());
            jpa.setAnchoBanda(p.getAnchoBanda());
            jpa.setCuentasEmail(p.getCuentasEmail());
            jpa.setSqlServerIncluido(p.isSqlServerIncluido());
            jpa.setIisVersion(p.getIisVersion());
            return jpa;
        }
        if (domain instanceof PlanPlataUnix p) {
            PlanPlataUnixJpa jpa = new PlanPlataUnixJpa();
            jpa.setId(p.getId());
            jpa.setNombre(p.getNombre());
            jpa.setPrecioMensual(p.getPrecioMensual());
            jpa.setEspacioDisco(p.getEspacioDisco());
            jpa.setAnchoBanda(p.getAnchoBanda());
            jpa.setCuentasEmail(p.getCuentasEmail());
            return jpa;
        }
        if (domain instanceof PlanPlataWindows p) {
            PlanPlataWindowsJpa jpa = new PlanPlataWindowsJpa();
            jpa.setId(p.getId());
            jpa.setNombre(p.getNombre());
            jpa.setPrecioMensual(p.getPrecioMensual());
            jpa.setEspacioDisco(p.getEspacioDisco());
            jpa.setAnchoBanda(p.getAnchoBanda());
            jpa.setCuentasEmail(p.getCuentasEmail());
            return jpa;
        }
        if (domain instanceof PlanOroUnix p) {
            PlanOroUnixJpa jpa = new PlanOroUnixJpa();
            jpa.setId(p.getId());
            jpa.setNombre(p.getNombre());
            jpa.setPrecioMensual(p.getPrecioMensual());
            jpa.setEspacioDisco(p.getEspacioDisco());
            jpa.setAnchoBanda(p.getAnchoBanda());
            jpa.setCuentasEmail(p.getCuentasEmail());
            jpa.setPythonIncluido(p.isPythonIncluido());
            return jpa;
        }
        if (domain instanceof PlanOroWindows p) {
            PlanOroWindowsJpa jpa = new PlanOroWindowsJpa();
            jpa.setId(p.getId());
            jpa.setNombre(p.getNombre());
            jpa.setPrecioMensual(p.getPrecioMensual());
            jpa.setEspacioDisco(p.getEspacioDisco());
            jpa.setAnchoBanda(p.getAnchoBanda());
            jpa.setCuentasEmail(p.getCuentasEmail());
            jpa.setAspNetVersion(p.getAspNetVersion());
            return jpa;
        }

        throw new IllegalArgumentException("Unknown plan type: " + domain.getClass());
    }

    public HostingPlan toDomain(HostingPlanJpa jpa) {
        if (jpa == null) return null;

        if (jpa instanceof PlanPlatinoUnixJpa j) {
            return new PlanPlatinoUnix(
                    j.getId(), j.getNombre(), j.getPrecioMensual(),
                    j.getEspacioDisco(), j.getAnchoBanda(), j.getCuentasEmail(),
                    j.isMysqlIncluido(), j.getPhpVersion());
        }
        if (jpa instanceof PlanPlatinoWindowsJpa j) {
            return new PlanPlatinoWindows(
                    j.getId(), j.getNombre(), j.getPrecioMensual(),
                    j.getEspacioDisco(), j.getAnchoBanda(), j.getCuentasEmail(),
                    j.isSqlServerIncluido(), j.getIisVersion());
        }
        if (jpa instanceof PlanPlataUnixJpa j) {
            return new PlanPlataUnix(
                    j.getId(), j.getNombre(), j.getPrecioMensual(),
                    j.getEspacioDisco(), j.getAnchoBanda(), j.getCuentasEmail());
        }
        if (jpa instanceof PlanPlataWindowsJpa j) {
            return new PlanPlataWindows(
                    j.getId(), j.getNombre(), j.getPrecioMensual(),
                    j.getEspacioDisco(), j.getAnchoBanda(), j.getCuentasEmail());
        }
        if (jpa instanceof PlanOroUnixJpa j) {
            return new PlanOroUnix(
                    j.getId(), j.getNombre(), j.getPrecioMensual(),
                    j.getEspacioDisco(), j.getAnchoBanda(), j.getCuentasEmail(),
                    j.isPythonIncluido());
        }
        if (jpa instanceof PlanOroWindowsJpa j) {
            return new PlanOroWindows(
                    j.getId(), j.getNombre(), j.getPrecioMensual(),
                    j.getEspacioDisco(), j.getAnchoBanda(), j.getCuentasEmail(),
                    j.getAspNetVersion());
        }

        throw new IllegalArgumentException("Unknown JPA type: " + jpa.getClass());
    }
}
