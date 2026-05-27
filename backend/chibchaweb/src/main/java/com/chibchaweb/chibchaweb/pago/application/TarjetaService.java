package com.chibchaweb.chibchaweb.pago.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.pago.domain.IntentoManager;
import com.chibchaweb.chibchaweb.pago.domain.IntentoManager.ResultadoIntento;
import com.chibchaweb.chibchaweb.pago.domain.LuhnValidator;
import com.chibchaweb.chibchaweb.pago.domain.TipoTarjeta;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.TarjetaCreditoRequest;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.TarjetaCreditoResponse;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.NumeroTarjetaInvalidoException;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.TarjetaCreditoJpa;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.TarjetaCreditoJpaRepository;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.UsuarioNoEncontradoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpa;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpaRepository;

@Service
@Transactional
public class TarjetaService {

    private final TarjetaCreditoJpaRepository tarjetaRepo;
    private final ClienteJpaRepository clienteRepo;
    private final IntentoManager intentoManager;

    public TarjetaService(TarjetaCreditoJpaRepository tarjetaRepo,
                          ClienteJpaRepository clienteRepo,
                          IntentoManager intentoManager) {
        this.tarjetaRepo = tarjetaRepo;
        this.clienteRepo = clienteRepo;
        this.intentoManager = intentoManager;
    }

    public TarjetaCreditoResponse asociar(Long clienteId, TarjetaCreditoRequest request) {
        ResultadoIntento resultado = intentoManager.registrarIntento(clienteId);

        if (resultado.limiteExcedido()) {
            throw new IntentoLimiteExcedidoException();
        }

        String numero = request.numero().replaceAll("\\s+", "");

        if (!LuhnValidator.validar(numero)) {
            throw new NumeroTarjetaInvalidoException(
                "Número de tarjeta inválido", resultado.intentosRestantes());
        }

        TipoTarjeta tipo = LuhnValidator.detectarTipo(numero);
        if (tipo == null) {
            throw new NumeroTarjetaInvalidoException(
                "Tipo de tarjeta no reconocido", resultado.intentosRestantes());
        }

        ClienteJpa cliente = clienteRepo.findById(clienteId)
                .orElseThrow(() -> UsuarioNoEncontradoException.porId(clienteId));

        TarjetaCreditoJpa jpa = new TarjetaCreditoJpa();
        jpa.setTitular(request.titular().trim());
        jpa.setNumero(numero);
        jpa.setFechaVencimiento(request.fechaVencimiento().trim());
        jpa.setCvv(request.cvv().trim());
        jpa.setTipoTarjeta(tipo);
        jpa.setCliente(cliente);

        TarjetaCreditoJpa saved = tarjetaRepo.save(jpa);

        intentoManager.resetear(clienteId);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TarjetaCreditoResponse> listarPorCliente(Long clienteId) {
        return tarjetaRepo.findByClienteId(clienteId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void eliminar(Long id, Long clienteId) {
        TarjetaCreditoJpa jpa = tarjetaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        if (!jpa.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("La tarjeta no pertenece al cliente");
        }
        tarjetaRepo.deleteById(id);
    }

    private TarjetaCreditoResponse toResponse(TarjetaCreditoJpa jpa) {
        String numero = jpa.getNumero();
        String enmascarado = "**** **** **** " + numero.substring(Math.max(0, numero.length() - 4));
        return new TarjetaCreditoResponse(
            jpa.getId(),
            jpa.getTitular(),
            enmascarado,
            jpa.getFechaVencimiento(),
            jpa.getTipoTarjeta().name()
        );
    }
}
