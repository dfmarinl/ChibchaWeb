package com.chibchaweb.chibchaweb.usuario.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;
import com.chibchaweb.chibchaweb.dominio.infrastructure.persistence.DominioJpaRepository;
import com.chibchaweb.chibchaweb.dominio.infrastructure.persistence.SitioWebJpaRepository;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.PagoJpaRepository;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.SuscripcionJpaRepository;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.TarjetaCreditoJpaRepository;
import com.chibchaweb.chibchaweb.soporte.infrastructure.persistence.TicketJpaRepository;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.domain.RegistroIntentoManager;
import com.chibchaweb.chibchaweb.usuario.domain.UsuarioFactory;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.ActualizarClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.ClienteResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.DocumentoDuplicadoException;
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
    private final SitioWebJpaRepository sitioWebRepository;
    private final DominioJpaRepository dominioRepository;
    private final TicketJpaRepository ticketRepository;
    private final SuscripcionJpaRepository suscripcionRepository;
    private final PagoJpaRepository pagoRepository;
    private final TarjetaCreditoJpaRepository tarjetaRepository;
    private final RegistroIntentoManager registroIntentoManager;

    public ClienteService(UsuarioFactory factory, ClienteDataMapper clienteMapper,
                          ClienteJpaRepository clienteJpaRepository,
                          ClienteDtoMapper dtoMapper,
                          SitioWebJpaRepository sitioWebRepository,
                          DominioJpaRepository dominioRepository,
                          TicketJpaRepository ticketRepository,
                          SuscripcionJpaRepository suscripcionRepository,
                          PagoJpaRepository pagoRepository,
                          TarjetaCreditoJpaRepository tarjetaRepository,
                          RegistroIntentoManager registroIntentoManager) {
        this.factory = factory;
        this.clienteMapper = clienteMapper;
        this.clienteJpaRepository = clienteJpaRepository;
        this.dtoMapper = dtoMapper;
        this.sitioWebRepository = sitioWebRepository;
        this.dominioRepository = dominioRepository;
        this.ticketRepository = ticketRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.pagoRepository = pagoRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.registroIntentoManager = registroIntentoManager;
    }

    public ClienteResponse crear(CrearClienteRequest request) {
        String email = request.email();

        if (clienteJpaRepository.findByEmail(email).isPresent()) {
            var resultado = registroIntentoManager.registrarIntento(email);
            if (resultado.limiteExcedido()) {
                throw new IntentoLimiteExcedidoException();
            }
            throw new EmailDuplicadoException(email, resultado.intentosRestantes());
        }

        if (clienteJpaRepository.findByDocumentoIdentidad(request.documentoIdentidad()).isPresent()) {
            var resultado = registroIntentoManager.registrarIntento(email);
            if (resultado.limiteExcedido()) {
                throw new IntentoLimiteExcedidoException();
            }
            throw new DocumentoDuplicadoException(request.documentoIdentidad(), resultado.intentosRestantes());
        }

        registroIntentoManager.resetear(email);

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

        if (request.documentoIdentidad() != null) {
            clienteJpaRepository.findByDocumentoIdentidad(request.documentoIdentidad())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new DocumentoDuplicadoException(request.documentoIdentidad()); });
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

        sitioWebRepository.deleteByPropietarioId(id);
        ticketRepository.deleteByClienteId(id);
        dominioRepository.deleteByPropietarioId(id);
        suscripcionRepository.deleteByClienteId(id);
        pagoRepository.deleteByClienteId(id);
        tarjetaRepository.deleteByClienteId(id);

        clienteMapper.delete(id);
    }
}
