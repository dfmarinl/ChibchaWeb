package com.chibchaweb.chibchaweb.pago.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.pago.domain.EstrategiaPago;
import com.chibchaweb.chibchaweb.pago.domain.IntentoManager;
import com.chibchaweb.chibchaweb.pago.domain.IntentoManager.ResultadoIntento;
import com.chibchaweb.chibchaweb.pago.domain.Pago;
import com.chibchaweb.chibchaweb.pago.domain.PagoDiners;
import com.chibchaweb.chibchaweb.pago.domain.PagoMastercard;
import com.chibchaweb.chibchaweb.pago.domain.PagoVisa;
import com.chibchaweb.chibchaweb.pago.domain.Periodicidad;
import com.chibchaweb.chibchaweb.pago.domain.ResultadoPago;
import com.chibchaweb.chibchaweb.pago.domain.TarjetaCredito;
import com.chibchaweb.chibchaweb.pago.domain.TipoTarjeta;

import com.chibchaweb.chibchaweb.pago.infrastructure.dto.PagoResponse;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.PagoDataMapper;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.PagoJpaRepository;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.TarjetaCreditoDataMapper;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;

@Service
@Transactional
public class PagoService {

    private static final double MONTO_LIMITE_SIMULACION = 500;

    private final PagoDataMapper pagoMapper;
    private final PagoJpaRepository pagoJpaRepository;
    private final TarjetaCreditoDataMapper tarjetaMapper;
    private final ClienteDataMapper clienteMapper;
    private final IntentoManager intentoManager;

    public PagoService(PagoDataMapper pagoMapper, PagoJpaRepository pagoJpaRepository,
                       TarjetaCreditoDataMapper tarjetaMapper,
                       ClienteDataMapper clienteMapper,
                       IntentoManager intentoManager) {
        this.pagoMapper = pagoMapper;
        this.pagoJpaRepository = pagoJpaRepository;
        this.tarjetaMapper = tarjetaMapper;
        this.clienteMapper = clienteMapper;
        this.intentoManager = intentoManager;
    }

    public PagoResponse procesarPagoCompra(Long clienteId, Long tarjetaId, double precioMensual,
                                            Periodicidad periodicidad) {
        ResultadoIntento resultado = intentoManager.registrarIntento(clienteId);
        if (resultado.limiteExcedido()) {
            throw new IntentoLimiteExcedidoException();
        }

        double monto = periodicidad.calcularMonto(precioMensual);

        Cliente cliente = clienteMapper.findById(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        TarjetaCredito tarjeta = tarjetaMapper.findById(tarjetaId);
        if (tarjeta == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada");
        }

        if (!tarjeta.getClienteId().equals(clienteId)) {
            throw new IllegalArgumentException("La tarjeta no pertenece al cliente");
        }

        validarDatosPago(monto, cliente, tarjeta);

        simularConfirmacion(monto);

        EstrategiaPago estrategia = crearEstrategia(tarjeta.getTipoTarjeta());

        Pago pago = new Pago(null, monto, cliente, tarjeta, estrategia, periodicidad);

        enviarSolicitud(pago);

        pagoMapper.insert(pago);

        intentoManager.resetear(clienteId);

        return toResponse(pago);
    }

    private void validarDatosPago(double monto, Cliente cliente, TarjetaCredito tarjeta) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (tarjeta == null) {
            throw new IllegalArgumentException("La tarjeta no puede ser nula");
        }
        String fechaVenc = tarjeta.getFechaVencimiento();
        if (fechaVenc == null || !fechaVenc.matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Fecha de vencimiento inválida");
        }
    }

    private void simularConfirmacion(double monto) {
        if (monto > MONTO_LIMITE_SIMULACION) {
            throw new IllegalArgumentException(
                "La simulación de confirmación ha fallado: el monto $" + String.format("%.2f", monto)
                    + " excede el límite de $" + String.format("%.0f", MONTO_LIMITE_SIMULACION));
        }
    }

    private void enviarSolicitud(Pago pago) {
        ResultadoPago resultadoPago = pago.ejecutarPago();
        if (resultadoPago.isExitoso()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Procesamiento de pago interrumpido", e);
            }
        }
    }

    private EstrategiaPago crearEstrategia(TipoTarjeta tipo) {
        return switch (tipo) {
            case VISA -> new PagoVisa();
            case MASTERCARD -> new PagoMastercard();
            case DINERS -> new PagoDiners();
        };
    }

    @Transactional(readOnly = true)
    public int consultarIntentosRestantes(Long clienteId) {
        return intentoManager.consultarIntentos(clienteId).intentosRestantes();
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarTodos() {
        return pagoJpaRepository.findAllByOrderByFechaDesc().stream()
                .map(jpa -> toResponse(pagoMapper.toDomain(jpa)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorCliente(Long clienteId) {
        return pagoJpaRepository.findByClienteIdOrderByFechaDesc(clienteId).stream()
                .map(jpa -> toResponse(pagoMapper.toDomain(jpa)))
                .toList();
    }

    private PagoResponse toResponse(Pago pago) {
        String tarjetaNum = pago.getTarjeta().getNumero();
        String enmascarado = "**** **** **** " + tarjetaNum.substring(Math.max(0, tarjetaNum.length() - 4));
        return new PagoResponse(
            pago.getId(),
            pago.getMonto(),
            pago.getReferencia(),
            pago.getEstado().name(),
            pago.getFecha(),
            enmascarado,
            pago.getTarjeta().getTipoTarjeta().name(),
            pago.getCliente().getNombre(),
            pago.getPeriodicidad().name()
        );
    }
}
