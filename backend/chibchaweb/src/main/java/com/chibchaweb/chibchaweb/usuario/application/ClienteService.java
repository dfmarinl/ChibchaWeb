package com.chibchaweb.chibchaweb.usuario.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.domain.ClienteFactory;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.ActualizarClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.ClienteResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.DocumentoDuplicadoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.EmailDuplicadoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.UsuarioNoEncontradoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.mapper.ClienteDtoMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpa;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpaRepository;

@Service
@Transactional
public class ClienteService {

    private final ClienteFactory factory;
    private final ClienteJpaRepository jpaRepository;
    private final ClienteDtoMapper dtoMapper;

    public ClienteService(ClienteFactory factory, ClienteJpaRepository jpaRepository,
                          ClienteDtoMapper dtoMapper) {
        this.factory = factory;
        this.jpaRepository = jpaRepository;
        this.dtoMapper = dtoMapper;
    }

    public ClienteResponse crear(CrearClienteRequest request) {
        if (jpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email());
        }
        Cliente cliente = factory.crearCliente(
                null, request.nombre(), request.email(), request.telefono(),
                request.direccion(), request.documentoIdentidad(), request.region());
        ClienteJpa jpa = toJpa(cliente);
        ClienteJpa saved = jpaRepository.save(jpa);
        return dtoMapper.toResponse(toDomain(saved));
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        ClienteJpa jpa = jpaRepository.findById(id)
                .orElseThrow(() -> UsuarioNoEncontradoException.porId(id));
        return dtoMapper.toResponse(toDomain(jpa));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .map(dtoMapper::toResponse)
                .toList();
    }

    public ClienteResponse actualizar(Long id, ActualizarClienteRequest request) {
        ClienteJpa jpa = jpaRepository.findById(id)
                .orElseThrow(() -> UsuarioNoEncontradoException.porId(id));
        if (request.nombre() != null) jpa.setNombre(request.nombre().trim());
        if (request.email() != null) {
            jpaRepository.findByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new EmailDuplicadoException(request.email()); });
            jpa.setEmail(request.email().trim().toLowerCase());
        }
        if (request.telefono() != null) jpa.setTelefono(request.telefono().trim());
        if (request.direccion() != null) jpa.setDireccion(request.direccion().trim());
        if (request.documentoIdentidad() != null) jpa.setDocumentoIdentidad(request.documentoIdentidad().trim());
        if (request.region() != null) jpa.setRegion(request.region().trim());
        ClienteJpa saved = jpaRepository.save(jpa);
        return dtoMapper.toResponse(toDomain(saved));
    }

    public void eliminar(Long id) {
        if (!jpaRepository.existsById(id)) {
            throw UsuarioNoEncontradoException.porId(id);
        }
        jpaRepository.deleteById(id);
    }

    private ClienteJpa toJpa(Cliente domain) {
        if (domain == null) return null;
        ClienteJpa jpa = new ClienteJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setEmail(domain.getEmail());
        jpa.setTelefono(domain.getTelefono());
        jpa.setFechaRegistro(domain.getFechaRegistro());
        jpa.setDireccion(domain.getDireccion());
        jpa.setDocumentoIdentidad(domain.getDocumentoIdentidad());
        jpa.setRegion(domain.getRegion());
        jpa.setLimitesSitios(domain.getLimitesSitios());
        return jpa;
    }

    private Cliente toDomain(ClienteJpa jpa) {
        if (jpa == null) return null;
        return new Cliente(
                jpa.getId(), jpa.getNombre(), jpa.getEmail(), jpa.getTelefono(),
                jpa.getDireccion(), jpa.getDocumentoIdentidad(), jpa.getRegion());
    }
}
