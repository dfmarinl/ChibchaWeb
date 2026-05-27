package com.chibchaweb.chibchaweb.pago.presentation;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chibchaweb.chibchaweb.pago.application.TarjetaService;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.TarjetaCreditoRequest;
import com.chibchaweb.chibchaweb.pago.infrastructure.dto.TarjetaCreditoResponse;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.NumeroTarjetaInvalidoException;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaCreditoController {

    private final TarjetaService service;

    public TarjetaCreditoController(TarjetaService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> asociar(@Valid @RequestBody TarjetaCreditoRequest request) {
        Long clienteId = (Long) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        try {
            TarjetaCreditoResponse response = service.asociar(clienteId, request);
            return ResponseEntity.ok(Map.of(
                "exitoso", true,
                "mensaje", "Tarjeta asociada exitosamente",
                "tarjeta", response
            ));
        } catch (IntentoLimiteExcedidoException e) {
            return ResponseEntity.status(423).body(Map.of(
                "exitoso", false,
                "mensaje", e.getMessage(),
                "limiteExcedido", true
            ));
        } catch (NumeroTarjetaInvalidoException e) {
            return ResponseEntity.status(422).body(Map.of(
                "exitoso", false,
                "mensaje", e.getMessage(),
                "intentosRestantes", e.getIntentosRestantes()
            ));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<TarjetaCreditoResponse>> listar() {
        Long clienteId = (Long) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Long clienteId = (Long) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        service.eliminar(id, clienteId);
        return ResponseEntity.noContent().build();
    }
}
