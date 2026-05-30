package com.chibchaweb.chibchaweb.usuario.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.domain.UsuarioFactory;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.ActualizarClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.ClienteResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.EmailDuplicadoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.UsuarioNoEncontradoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.mapper.ClienteDtoMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpaRepository;

@Service
@Transactional
public class ClienteService {

    private final UsuarioFactory factory;
    private final ClienteDataMapper clienteMapper;
    private final ClienteJpaRepository clienteJpaRepository;
    private final ClienteDtoMapper dtoMapper;

    public ClienteService(UsuarioFactory factory, ClienteDataMapper clienteMapper,
                          ClienteJpaRepository clienteJpaRepository,
                          ClienteDtoMapper dtoMapper) {
        this.factory = factory;
        this.clienteMapper = clienteMapper;
        this.clienteJpaRepository = clienteJpaRepository;
        this.dtoMapper = dtoMapper;
    }

    public ClienteResponse crear(CrearClienteRequest request) {
        if (clienteJpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email());
        }
        Cliente cliente = factory.crearUsuario(NombreRol.CLIENTE, request);
        var saved = clienteJpaRepository.save(clienteMapper.toJpa(cliente));
        return dtoMapper.toResponse(clienteMapper.toDomain(saved));
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = clienteMapper.findById(id);
        if (cliente == null) throw UsuarioNoEncontradoException.porId(id);
        return dtoMapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorEmail(String email) {
        var jpa = clienteJpaRepository.findByEmail(email)
                .orElseThrow(() -> UsuarioNoEncontradoException.porEmail(email));
        return dtoMapper.toResponse(clienteMapper.toDomain(jpa));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteMapper.findAll().stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

    public ClienteResponse actualizar(Long id, ActualizarClienteRequest request) {
        Cliente cliente = clienteMapper.findById(id);
        if (cliente == null) throw UsuarioNoEncontradoException.porId(id);

        if (request.email() != null) {
            clienteJpaRepository.findByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new EmailDuplicadoException(request.email()); });
        }

        Cliente merged = new Cliente(
            id,
            request.nombre() != null ? request.nombre().trim() : cliente.getNombre(),
            request.email() != null ? request.email().trim().toLowerCase() : cliente.getEmail(),
            request.telefono() != null ? request.telefono().trim() : cliente.getTelefono(),
            request.direccion() != null ? request.direccion().trim() : cliente.getDireccion(),
            request.documentoIdentidad() != null ? request.documentoIdentidad().trim() : cliente.getDocumentoIdentidad(),
            request.region() != null ? request.region().trim() : cliente.getRegion()
        );
        clienteMapper.update(merged);
        Cliente actualizado = clienteMapper.findById(id);
        return dtoMapper.toResponse(actualizado);
    }

    public void eliminar(Long id) {
        Cliente cliente = clienteMapper.findById(id);
        if (cliente == null) throw UsuarioNoEncontradoException.porId(id);
        clienteMapper.delete(id);
    }
}
