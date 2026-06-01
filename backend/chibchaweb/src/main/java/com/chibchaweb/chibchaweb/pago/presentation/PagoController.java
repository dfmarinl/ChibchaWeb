package com.chibchaweb.chibchaweb.pago.presentation;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chibchaweb.chibchaweb.pago.application.PagoService;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.PagoResponse;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.usuario.application.ClienteService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final ClienteService clienteService;

    public PagoController(PagoService pagoService, ClienteService clienteService) {
        this.pagoService = pagoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<List<PagoResponse>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/intentos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> consultarIntentos() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long clienteId = clienteService.buscarPorEmail(email).id();
        int restantes = pagoService.consultarIntentosRestantes(clienteId);
        return ResponseEntity.ok(Map.of(
            "exitoso", true,
            "intentosRestantes", restantes,
            "limiteExcedido", restantes <= 0
        ));
    }

    @GetMapping("/mis-pagos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> listarMisPagos() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long clienteId = clienteService.buscarPorEmail(email).id();
        return ResponseEntity.ok(Map.of(
            "exitoso", true,
            "pagos", pagoService.listarPorCliente(clienteId)
        ));
    }
}
