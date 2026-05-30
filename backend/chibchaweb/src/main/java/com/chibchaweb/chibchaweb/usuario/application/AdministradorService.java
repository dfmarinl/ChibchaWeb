package com.chibchaweb.chibchaweb.usuario.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;
import com.chibchaweb.chibchaweb.usuario.domain.Administrador;
import com.chibchaweb.chibchaweb.usuario.domain.UsuarioFactory;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.ActualizarAdministradorRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearAdministradorRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.AdministradorResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.EmailDuplicadoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.UsuarioNoEncontradoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.mapper.AdministradorDtoMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.AdministradorDataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.AdministradorJpaRepository;

@Service
@Transactional
public class AdministradorService {

    private final UsuarioFactory factory;
    private final AdministradorDataMapper administradorMapper;
    private final AdministradorJpaRepository administradorJpaRepository;
    private final AdministradorDtoMapper dtoMapper;

    public AdministradorService(UsuarioFactory factory,
                                AdministradorDataMapper administradorMapper,
                                AdministradorJpaRepository administradorJpaRepository,
                                AdministradorDtoMapper dtoMapper) {
        this.factory = factory;
        this.administradorMapper = administradorMapper;
        this.administradorJpaRepository = administradorJpaRepository;
        this.dtoMapper = dtoMapper;
    }

    public AdministradorResponse crear(CrearAdministradorRequest request) {
        if (administradorJpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email());
        }
        Administrador admin = factory.crearUsuario(NombreRol.ADMINISTRADOR, request);
        var saved = administradorJpaRepository.save(administradorMapper.toJpa(admin));
        return dtoMapper.toResponse(administradorMapper.toDomain(saved));
    }

    @Transactional(readOnly = true)
    public AdministradorResponse buscarPorId(Long id) {
        Administrador admin = administradorMapper.findById(id);
        if (admin == null) throw UsuarioNoEncontradoException.porId(id);
        return dtoMapper.toResponse(admin);
    }

    @Transactional(readOnly = true)
    public List<AdministradorResponse> listarTodos() {
        return administradorMapper.findAll().stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

    public AdministradorResponse actualizar(Long id, ActualizarAdministradorRequest request) {
        Administrador admin = administradorMapper.findById(id);
        if (admin == null) throw UsuarioNoEncontradoException.porId(id);

        if (request.email() != null) {
            administradorJpaRepository.findByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new EmailDuplicadoException(request.email()); });
        }

        Administrador merged = new Administrador(
            id,
            request.nombre() != null ? request.nombre().trim() : admin.getNombre(),
            request.email() != null ? request.email().trim().toLowerCase() : admin.getEmail(),
            request.telefono() != null ? request.telefono().trim() : admin.getTelefono(),
            request.nivelAcceso() != null ? request.nivelAcceso().trim().toUpperCase() : admin.getNivelAcceso()
        );
        administradorMapper.update(merged);
        Administrador actualizado = administradorMapper.findById(id);
        return dtoMapper.toResponse(actualizado);
    }

    public void eliminar(Long id) {
        Administrador admin = administradorMapper.findById(id);
        if (admin == null) throw UsuarioNoEncontradoException.porId(id);
        administradorMapper.delete(id);
    }
}
