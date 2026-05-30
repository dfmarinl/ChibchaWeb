package com.chibchaweb.chibchaweb.dominio.presentation;

import jakarta.validation.Valid;
import java.util.List;
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
import com.chibchaweb.chibchaweb.dominio.application.SitioWebService;
import com.chibchaweb.chibchaweb.dominio.infrastructure.dto.SitioWebRequest;
import com.chibchaweb.chibchaweb.dominio.infrastructure.dto.SitioWebResponse;
import com.chibchaweb.chibchaweb.usuario.application.ClienteService;

@RestController
@RequestMapping("/api/sitios-web")
public class SitioWebController {

    private final SitioWebService service;
    private final ClienteService clienteService;

    public SitioWebController(SitioWebService service, ClienteService clienteService) {
        this.service = service;
        this.clienteService = clienteService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<SitioWebResponse> crear(@Valid @RequestBody SitioWebRequest request) {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long clienteId = clienteService.buscarPorEmail(email).id();
        SitioWebResponse response = service.crear(clienteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<SitioWebResponse>> listar() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long clienteId = clienteService.buscarPorEmail(email).id();
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long clienteId = clienteService.buscarPorEmail(email).id();
        service.eliminar(id, clienteId);
        return ResponseEntity.noContent().build();
    }
}
