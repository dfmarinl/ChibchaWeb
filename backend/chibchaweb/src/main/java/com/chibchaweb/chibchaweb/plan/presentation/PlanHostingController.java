package com.chibchaweb.chibchaweb.plan.presentation;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.plan.application.HostingPlanService;
import com.chibchaweb.chibchaweb.plan.infrastructure.dto.AsociarPlataformaRequest;
import com.chibchaweb.chibchaweb.plan.infrastructure.dto.CrearPlanRequest;
import com.chibchaweb.chibchaweb.plan.infrastructure.dto.ModificarPlanRequest;
import com.chibchaweb.chibchaweb.plan.infrastructure.dto.PlanHostingResponse;
import com.chibchaweb.chibchaweb.plan.infrastructure.exception.PlanNoEncontradoException;
import com.chibchaweb.chibchaweb.usuario.application.ClienteService;

@RestController
@RequestMapping("/api/planes")
public class PlanHostingController {

    private final HostingPlanService service;
    private final ClienteService clienteService;

    public PlanHostingController(HostingPlanService service, ClienteService clienteService) {
        this.service = service;
        this.clienteService = clienteService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<?> crear(@Valid @RequestBody CrearPlanRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        boolean isCliente = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
        Long clienteId = isCliente ? clienteService.buscarPorEmail(email).id() : null;
        try {
            PlanHostingResponse response = service.crear(clienteId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "exitoso", true,
                "mensaje", "Plan de hosting creado exitosamente",
                "plan", response
            ));
        } catch (IntentoLimiteExcedidoException e) {
            return ResponseEntity.status(423).body(Map.of(
                "exitoso", false,
                "mensaje", e.getMessage(),
                "limiteExcedido", true
            ));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<List<PlanHostingResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<PlanHostingResponse> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(service.consultar(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<PlanHostingResponse> modificar(@PathVariable Long id,
                                                          @Valid @RequestBody ModificarPlanRequest request) {
        return ResponseEntity.ok(service.modificar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/asociar-plataforma")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<?> asociarPlataforma(@PathVariable Long id,
                                                @Valid @RequestBody AsociarPlataformaRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        boolean isCliente = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
        Long clienteId = isCliente ? clienteService.buscarPorEmail(email).id() : null;
        try {
            PlanHostingResponse response = service.asociarPlataforma(id, clienteId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "exitoso", true,
                "mensaje", "Plataforma asociada exitosamente",
                "plan", response
            ));
        } catch (IntentoLimiteExcedidoException e) {
            return ResponseEntity.status(423).body(Map.of(
                "exitoso", false,
                "mensaje", e.getMessage(),
                "limiteExcedido", true
            ));
        }
    }
}
