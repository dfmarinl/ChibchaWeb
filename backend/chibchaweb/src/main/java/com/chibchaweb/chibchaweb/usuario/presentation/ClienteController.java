package com.chibchaweb.chibchaweb.usuario.presentation;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chibchaweb.chibchaweb.usuario.application.ClienteService;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.ActualizarClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.ClienteResponse;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody CrearClienteRequest request) {
        ClienteResponse response = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponse> perfilActual() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE')")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody ActualizarClienteRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
