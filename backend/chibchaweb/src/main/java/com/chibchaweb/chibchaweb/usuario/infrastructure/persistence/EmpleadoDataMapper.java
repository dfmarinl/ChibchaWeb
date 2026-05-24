package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;
import com.chibchaweb.chibchaweb.usuario.domain.Empleado;

@Component
public class EmpleadoDataMapper implements DataMapper<Empleado, Long> {

    private final EmpleadoJpaRepository repository;

    public EmpleadoDataMapper(EmpleadoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Empleado entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(Empleado entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Empleado findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Empleado> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private EmpleadoJpa toJpa(Empleado domain) {
        if (domain == null) return null;
        EmpleadoJpa jpa = new EmpleadoJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setEmail(domain.getEmail());
        jpa.setTelefono(domain.getTelefono());
        jpa.setFechaRegistro(domain.getFechaRegistro());
        jpa.setCargo(domain.getCargo());
        jpa.setDepartamento(domain.getDepartamento());
        jpa.setSalario(domain.getSalario());
        jpa.setFechaContratacion(domain.getFechaContratacion());
        return jpa;
    }

    private Empleado toDomain(EmpleadoJpa jpa) {
        if (jpa == null) return null;
        return new Empleado(
            jpa.getId(),
            jpa.getNombre(),
            jpa.getEmail(),
            jpa.getTelefono(),
            jpa.getCargo(),
            jpa.getDepartamento(),
            jpa.getSalario()
        );
    }
}
