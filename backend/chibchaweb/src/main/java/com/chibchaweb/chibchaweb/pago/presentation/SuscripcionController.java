package com.chibchaweb.chibchaweb.pago.presentation;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chibchaweb.chibchaweb.pago.application.PagoService;
import com.chibchaweb.chibchaweb.pago.application.SuscripcionService;
import com.chibchaweb.chibchaweb.pago.domain.Pago;
import com.chibchaweb.chibchaweb.pago.domain.Periodicidad;
import com.chibchaweb.chibchaweb.pago.domain.Suscripcion;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.PagoResponse;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.RenovarSuscripcionRequest;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.SuscripcionResponse;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.PagoDataMapper;
import com.chibchaweb.chibchaweb.usuario.application.ClienteService;

@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;
    private final ClienteService clienteService;
    private final PagoService pagoService;
    private final PagoDataMapper pagoMapper;

    public SuscripcionController(SuscripcionService suscripcionService, ClienteService clienteService,
                                  PagoService pagoService, PagoDataMapper pagoMapper) {
        this.suscripcionService = suscripcionService;
        this.clienteService = clienteService;
        this.pagoService = pagoService;
        this.pagoMapper = pagoMapper;
    }

    @GetMapping("/mis-suscripciones")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> listarMisSuscripciones() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        Long clienteId = clienteService.buscarPorEmail(email).id();
        List<SuscripcionResponse> suscripciones = suscripcionService.consultarPorCliente(clienteId);
        return ResponseEntity.ok(Map.of(
            "exitoso", true,
            "suscripciones", suscripciones
        ));
    }

    @PostMapping("/{id}/renovar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> renovar(@PathVariable Long id,
                                      @Valid @RequestBody RenovarSuscripcionRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        Long clienteId = clienteService.buscarPorEmail(email).id();

        try {
            Periodicidad periodicidad = Periodicidad.valueOf(request.periodicidad().toUpperCase());

            Suscripcion suscripcion = suscripcionService.buscarPorId(id);
            if (suscripcion == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "exitoso", false,
                    "mensaje", "Suscripción no encontrada"
                ));
            }

            double precioMensual = suscripcion.getPlanHosting().getPrecioMensual();

            PagoResponse pagoResponse = pagoService.procesarPagoCompra(clienteId, request.tarjetaId(),
                                                                         precioMensual, periodicidad);

            Pago pago = pagoMapper.findById(pagoResponse.id());
            SuscripcionResponse suscripcionResponse = suscripcionService.renovar(id, periodicidad, pago);

            return ResponseEntity.ok(Map.of(
                "exitoso", true,
                "mensaje", "Suscripción renovada exitosamente",
                "suscripcion", suscripcionResponse
            ));
        } catch (IntentoLimiteExcedidoException e) {
            return ResponseEntity.status(423).body(Map.of(
                "exitoso", false,
                "mensaje", e.getMessage(),
                "limiteExcedido", true
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exitoso", false,
                "mensaje", e.getMessage()
            ));
        }
    }
}
