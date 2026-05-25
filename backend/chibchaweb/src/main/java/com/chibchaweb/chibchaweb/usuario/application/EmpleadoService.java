package com.chibchaweb.chibchaweb.usuario.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;
import com.chibchaweb.chibchaweb.usuario.domain.Empleado;
import com.chibchaweb.chibchaweb.usuario.domain.UsuarioFactory;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.ActualizarEmpleadoRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearEmpleadoRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.EmpleadoResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.EmailDuplicadoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.UsuarioNoEncontradoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.mapper.EmpleadoDtoMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.EmpleadoJpa;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.EmpleadoJpaRepository;

@Service
@Transactional
public class EmpleadoService {

    private final UsuarioFactory factory;
    private final EmpleadoJpaRepository jpaRepository;
    private final EmpleadoDtoMapper dtoMapper;

    public EmpleadoService(UsuarioFactory factory, EmpleadoJpaRepository jpaRepository,
                           EmpleadoDtoMapper dtoMapper) {
        this.factory = factory;
        this.jpaRepository = jpaRepository;
        this.dtoMapper = dtoMapper;
    }

    public EmpleadoResponse crear(CrearEmpleadoRequest request) {
        if (jpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email());
        }
        Empleado empleado = factory.crearUsuario(NombreRol.EMPLEADO, request);
        EmpleadoJpa jpa = toJpa(empleado);
        EmpleadoJpa saved = jpaRepository.save(jpa);
        return dtoMapper.toResponse(toDomain(saved));
    }

    @Transactional(readOnly = true)
    public EmpleadoResponse buscarPorId(Long id) {
        EmpleadoJpa jpa = jpaRepository.findById(id)
                .orElseThrow(() -> UsuarioNoEncontradoException.porId(id));
        return dtoMapper.toResponse(toDomain(jpa));
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .map(dtoMapper::toResponse)
                .toList();
    }

    public EmpleadoResponse actualizar(Long id, ActualizarEmpleadoRequest request) {
        EmpleadoJpa jpa = jpaRepository.findById(id)
                .orElseThrow(() -> UsuarioNoEncontradoException.porId(id));
        if (request.nombre() != null) jpa.setNombre(request.nombre().trim());
        if (request.email() != null) {
            jpaRepository.findByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new EmailDuplicadoException(request.email()); });
            jpa.setEmail(request.email().trim().toLowerCase());
        }
        if (request.telefono() != null) jpa.setTelefono(request.telefono().trim());
        if (request.cargo() != null) jpa.setCargo(request.cargo().trim());
        if (request.departamento() != null) jpa.setDepartamento(request.departamento().trim());
        if (request.salario() != null) jpa.setSalario(request.salario());
        EmpleadoJpa saved = jpaRepository.save(jpa);
        return dtoMapper.toResponse(toDomain(saved));
    }

    public void eliminar(Long id) {
        if (!jpaRepository.existsById(id)) {
            throw UsuarioNoEncontradoException.porId(id);
        }
        jpaRepository.deleteById(id);
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
                jpa.getId(), jpa.getNombre(), jpa.getEmail(), jpa.getTelefono(),
                jpa.getCargo(), jpa.getDepartamento(), jpa.getSalario());
    }
}
