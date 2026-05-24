package com.chibchaweb.chibchaweb.acceso.presentation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chibchaweb.chibchaweb.acceso.application.AutenticacionService;
import com.chibchaweb.chibchaweb.acceso.application.AutenticacionService.LoginResultado;
import com.chibchaweb.chibchaweb.acceso.application.RegistroService;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.LoginRequest;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.LoginResponse;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.RegistroAdministradorRequest;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.RegistroClienteRequest;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.RegistroEmpleadoRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AutenticacionService autenticacionService;
    private final RegistroService registroService;

    public AuthController(AutenticacionService autenticacionService,
                          RegistroService registroService) {
        this.autenticacionService = autenticacionService;
        this.registroService = registroService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResultado resultado = autenticacionService.login(
                request.email(), request.contrasena());

        LoginResponse response = new LoginResponse(
                resultado.token(),
                resultado.sesionId(),
                resultado.usuarioId(),
                resultado.email(),
                resultado.rol());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro/cliente")
    public ResponseEntity<LoginResponse> registrarCliente(
            @Valid @RequestBody RegistroClienteRequest request) {
        LoginResponse response = registroService.registrarCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/registro/empleado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<LoginResponse> registrarEmpleado(
            @Valid @RequestBody RegistroEmpleadoRequest request) {
        LoginResponse response = registroService.registrarEmpleado(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/registro/administrador")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<LoginResponse> registrarAdministrador(
            @Valid @RequestBody RegistroAdministradorRequest request) {
        LoginResponse response = registroService.registrarAdministrador(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
