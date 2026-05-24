package com.chibchaweb.chibchaweb.distribuidor.presentation;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chibchaweb.chibchaweb.distribuidor.application.DistribuidorService;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request.ActualizarDistribuidorRequest;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request.CrearDistribuidorRequest;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.response.DistribuidorResponse;

@RestController
@RequestMapping("/api/distribuidores")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class DistribuidorController {

    private final DistribuidorService service;

    public DistribuidorController(DistribuidorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DistribuidorResponse> crear(@Valid @RequestBody CrearDistribuidorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistribuidorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<DistribuidorResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistribuidorResponse> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody ActualizarDistribuidorRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
