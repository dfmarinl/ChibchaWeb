package com.chibchaweb.chibchaweb.usuario.presentation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chibchaweb.chibchaweb.usuario.domain.Administrador;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.domain.Empleado;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.UsuarioResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.AdministradorJpaRepository;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteJpaRepository;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.EmpleadoJpaRepository;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO')")
public class UsuarioController {

    private final ClienteJpaRepository clienteRepo;
    private final EmpleadoJpaRepository empleadoRepo;
    private final AdministradorJpaRepository adminRepo;

    public UsuarioController(ClienteJpaRepository clienteRepo,
                             EmpleadoJpaRepository empleadoRepo,
                             AdministradorJpaRepository adminRepo) {
        this.clienteRepo = clienteRepo;
        this.empleadoRepo = empleadoRepo;
        this.adminRepo = adminRepo;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> resultados = new ArrayList<>();

        clienteRepo.findAll().forEach(jpa ->
            resultados.add(new UsuarioResponse(
                    jpa.getId(), jpa.getNombre(), jpa.getEmail(),
                    jpa.getTelefono(), jpa.getFechaRegistro(), "CLIENTE")));

        empleadoRepo.findAll().forEach(jpa ->
            resultados.add(new UsuarioResponse(
                    jpa.getId(), jpa.getNombre(), jpa.getEmail(),
                    jpa.getTelefono(), jpa.getFechaRegistro(), "EMPLEADO")));

        adminRepo.findAll().forEach(jpa ->
            resultados.add(new UsuarioResponse(
                    jpa.getId(), jpa.getNombre(), jpa.getEmail(),
                    jpa.getTelefono(), jpa.getFechaRegistro(), "ADMINISTRADOR")));

        resultados.sort(Comparator.comparing(UsuarioResponse::id));
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        var cliente = clienteRepo.findById(id);
        if (cliente.isPresent()) {
            var jpa = cliente.get();
            return ResponseEntity.ok(new UsuarioResponse(
                    jpa.getId(), jpa.getNombre(), jpa.getEmail(),
                    jpa.getTelefono(), jpa.getFechaRegistro(), "CLIENTE"));
        }
        var empleado = empleadoRepo.findById(id);
        if (empleado.isPresent()) {
            var jpa = empleado.get();
            return ResponseEntity.ok(new UsuarioResponse(
                    jpa.getId(), jpa.getNombre(), jpa.getEmail(),
                    jpa.getTelefono(), jpa.getFechaRegistro(), "EMPLEADO"));
        }
        var admin = adminRepo.findById(id);
        if (admin.isPresent()) {
            var jpa = admin.get();
            return ResponseEntity.ok(new UsuarioResponse(
                    jpa.getId(), jpa.getNombre(), jpa.getEmail(),
                    jpa.getTelefono(), jpa.getFechaRegistro(), "ADMINISTRADOR"));
        }
        return ResponseEntity.notFound().build();
    }
}
