package com.chibchaweb.chibchaweb.usuario.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.usuario.domain.Administrador;
import com.chibchaweb.chibchaweb.usuario.domain.AdministradorFactory;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.ActualizarAdministradorRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearAdministradorRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.AdministradorResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.EmailDuplicadoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.UsuarioNoEncontradoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.mapper.AdministradorDtoMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.AdministradorJpa;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.AdministradorJpaRepository;

@Service
@Transactional
public class AdministradorService {

    private final AdministradorFactory factory;
    private final AdministradorJpaRepository jpaRepository;
    private final AdministradorDtoMapper dtoMapper;

    public AdministradorService(AdministradorFactory factory,
                                AdministradorJpaRepository jpaRepository,
                                AdministradorDtoMapper dtoMapper) {
        this.factory = factory;
        this.jpaRepository = jpaRepository;
        this.dtoMapper = dtoMapper;
    }

    public AdministradorResponse crear(CrearAdministradorRequest request) {
        if (jpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email());
        }
        Administrador admin = factory.crearAdministrador(
                null, request.nombre(), request.email(), request.telefono(),
                request.nivelAcceso());
        AdministradorJpa jpa = toJpa(admin);
        AdministradorJpa saved = jpaRepository.save(jpa);
        return dtoMapper.toResponse(toDomain(saved));
    }

    @Transactional(readOnly = true)
    public AdministradorResponse buscarPorId(Long id) {
        AdministradorJpa jpa = jpaRepository.findById(id)
                .orElseThrow(() -> UsuarioNoEncontradoException.porId(id));
        return dtoMapper.toResponse(toDomain(jpa));
    }

    @Transactional(readOnly = true)
    public List<AdministradorResponse> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .map(dtoMapper::toResponse)
                .toList();
    }

    public AdministradorResponse actualizar(Long id, ActualizarAdministradorRequest request) {
        AdministradorJpa jpa = jpaRepository.findById(id)
                .orElseThrow(() -> UsuarioNoEncontradoException.porId(id));
        if (request.nombre() != null) jpa.setNombre(request.nombre().trim());
        if (request.email() != null) {
            jpaRepository.findByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new EmailDuplicadoException(request.email()); });
            jpa.setEmail(request.email().trim().toLowerCase());
        }
        if (request.telefono() != null) jpa.setTelefono(request.telefono().trim());
        if (request.nivelAcceso() != null) jpa.setNivelAcceso(request.nivelAcceso().trim().toUpperCase());
        AdministradorJpa saved = jpaRepository.save(jpa);
        return dtoMapper.toResponse(toDomain(saved));
    }

    public void eliminar(Long id) {
        if (!jpaRepository.existsById(id)) {
            throw UsuarioNoEncontradoException.porId(id);
        }
        jpaRepository.deleteById(id);
    }

    private AdministradorJpa toJpa(Administrador domain) {
        if (domain == null) return null;
        AdministradorJpa jpa = new AdministradorJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setEmail(domain.getEmail());
        jpa.setTelefono(domain.getTelefono());
        jpa.setFechaRegistro(domain.getFechaRegistro());
        jpa.setNivelAcceso(domain.getNivelAcceso());
        return jpa;
    }

    private Administrador toDomain(AdministradorJpa jpa) {
        if (jpa == null) return null;
        return new Administrador(
                jpa.getId(), jpa.getNombre(), jpa.getEmail(), jpa.getTelefono(),
                jpa.getNivelAcceso());
    }
}
