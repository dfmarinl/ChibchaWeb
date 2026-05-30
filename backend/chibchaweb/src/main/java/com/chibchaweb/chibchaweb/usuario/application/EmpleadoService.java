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
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.EmpleadoDataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.EmpleadoJpaRepository;

@Service
@Transactional
public class EmpleadoService {

    private final UsuarioFactory factory;
    private final EmpleadoDataMapper empleadoMapper;
    private final EmpleadoJpaRepository empleadoJpaRepository;
    private final EmpleadoDtoMapper dtoMapper;

    public EmpleadoService(UsuarioFactory factory, EmpleadoDataMapper empleadoMapper,
                           EmpleadoJpaRepository empleadoJpaRepository,
                           EmpleadoDtoMapper dtoMapper) {
        this.factory = factory;
        this.empleadoMapper = empleadoMapper;
        this.empleadoJpaRepository = empleadoJpaRepository;
        this.dtoMapper = dtoMapper;
    }

    public EmpleadoResponse crear(CrearEmpleadoRequest request) {
        if (empleadoJpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email());
        }
        Empleado empleado = factory.crearUsuario(NombreRol.EMPLEADO, request);
        var saved = empleadoJpaRepository.save(empleadoMapper.toJpa(empleado));
        return dtoMapper.toResponse(empleadoMapper.toDomain(saved));
    }

    @Transactional(readOnly = true)
    public EmpleadoResponse buscarPorId(Long id) {
        Empleado empleado = empleadoMapper.findById(id);
        if (empleado == null) throw UsuarioNoEncontradoException.porId(id);
        return dtoMapper.toResponse(empleado);
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listarTodos() {
        return empleadoMapper.findAll().stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

    public EmpleadoResponse actualizar(Long id, ActualizarEmpleadoRequest request) {
        Empleado empleado = empleadoMapper.findById(id);
        if (empleado == null) throw UsuarioNoEncontradoException.porId(id);

        if (request.email() != null) {
            empleadoJpaRepository.findByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new EmailDuplicadoException(request.email()); });
        }

        Empleado merged = new Empleado(
            id,
            request.nombre() != null ? request.nombre().trim() : empleado.getNombre(),
            request.email() != null ? request.email().trim().toLowerCase() : empleado.getEmail(),
            request.telefono() != null ? request.telefono().trim() : empleado.getTelefono(),
            request.cargo() != null ? request.cargo().trim() : empleado.getCargo(),
            request.departamento() != null ? request.departamento().trim() : empleado.getDepartamento(),
            request.salario() != null ? request.salario() : empleado.getSalario()
        );
        empleadoMapper.update(merged);
        Empleado actualizado = empleadoMapper.findById(id);
        return dtoMapper.toResponse(actualizado);
    }

    public void eliminar(Long id) {
        Empleado empleado = empleadoMapper.findById(id);
        if (empleado == null) throw UsuarioNoEncontradoException.porId(id);
        empleadoMapper.delete(id);
    }
}
