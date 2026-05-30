package com.chibchaweb.chibchaweb.pago.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.pago.domain.IntentoManager;
import com.chibchaweb.chibchaweb.pago.domain.IntentoManager.ResultadoIntento;
import com.chibchaweb.chibchaweb.pago.domain.LuhnValidator;
import com.chibchaweb.chibchaweb.pago.domain.TarjetaCredito;
import com.chibchaweb.chibchaweb.pago.domain.TipoTarjeta;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.TarjetaCreditoRequest;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.TarjetaCreditoResponse;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.NumeroTarjetaInvalidoException;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.TarjetaCreditoDataMapper;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.TarjetaCreditoJpa;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.TarjetaCreditoJpaRepository;

@Service
@Transactional
public class TarjetaService {

    private final TarjetaCreditoDataMapper tarjetaMapper;
    private final TarjetaCreditoJpaRepository tarjetaJpaRepository;
    private final IntentoManager intentoManager;

    public TarjetaService(TarjetaCreditoDataMapper tarjetaMapper,
                          TarjetaCreditoJpaRepository tarjetaJpaRepository,
                          IntentoManager intentoManager) {
        this.tarjetaMapper = tarjetaMapper;
        this.tarjetaJpaRepository = tarjetaJpaRepository;
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

        TarjetaCredito tarjeta = new TarjetaCredito(null, request.titular().trim(), numero,
                request.fechaVencimiento().trim(), request.cvv().trim(), tipo, clienteId);

        var saved = tarjetaJpaRepository.save(tarjetaMapper.toJpa(tarjeta));

        intentoManager.resetear(clienteId);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TarjetaCreditoResponse> listarPorCliente(Long clienteId) {
        return tarjetaJpaRepository.findByClienteId(clienteId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void eliminar(Long id, Long clienteId) {
        TarjetaCredito tarjeta = tarjetaMapper.findById(id);
        if (tarjeta == null) throw new RuntimeException("Tarjeta no encontrada");
        if (!tarjeta.getClienteId().equals(clienteId)) {
            throw new RuntimeException("La tarjeta no pertenece al cliente");
        }
        tarjetaMapper.delete(id);
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
