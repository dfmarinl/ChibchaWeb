package com.chibchaweb.chibchaweb.acceso.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.CuentaAcceso;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;
import com.chibchaweb.chibchaweb.acceso.domain.Sesion;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.LoginResponse;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.RegistroAdministradorRequest;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.RegistroClienteRequest;
import com.chibchaweb.chibchaweb.acceso.infrastructure.dto.RegistroEmpleadoRequest;
import com.chibchaweb.chibchaweb.acceso.infrastructure.security.JwtTokenProvider;
import com.chibchaweb.chibchaweb.usuario.application.AdministradorService;
import com.chibchaweb.chibchaweb.usuario.application.ClienteService;
import com.chibchaweb.chibchaweb.usuario.application.EmpleadoService;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearAdministradorRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearEmpleadoRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.AdministradorResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.ClienteResponse;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.response.EmpleadoResponse;

@Service
@Transactional
public class RegistroService {

    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final AdministradorService administradorService;
    private final CuentaAccesoService cuentaAccesoService;
    private final SesionService sesionService;
    private final JwtTokenProvider jwtTokenProvider;

    public RegistroService(ClienteService clienteService,
                           EmpleadoService empleadoService,
                           AdministradorService administradorService,
                           CuentaAccesoService cuentaAccesoService,
                           SesionService sesionService,
                           JwtTokenProvider jwtTokenProvider) {
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
        this.administradorService = administradorService;
        this.cuentaAccesoService = cuentaAccesoService;
        this.sesionService = sesionService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse registrarCliente(RegistroClienteRequest request) {
        CrearClienteRequest crearReq = new CrearClienteRequest(
                request.nombre(), request.email(), request.telefono(),
                request.direccion(), request.documentoIdentidad(), request.region());
        ClienteResponse usuario = clienteService.crear(crearReq);
        return registrar(usuario.id(), request.email(), request.contrasena(), NombreRol.CLIENTE);
    }

    public LoginResponse registrarEmpleado(RegistroEmpleadoRequest request) {
        CrearEmpleadoRequest crearReq = new CrearEmpleadoRequest(
                request.nombre(), request.email(), request.telefono(),
                request.cargo(), request.departamento(), request.salario());
        EmpleadoResponse usuario = empleadoService.crear(crearReq);
        return registrar(usuario.id(), request.email(), request.contrasena(), NombreRol.EMPLEADO);
    }

    public LoginResponse registrarAdministrador(RegistroAdministradorRequest request) {
        CrearAdministradorRequest crearReq = new CrearAdministradorRequest(
                request.nombre(), request.email(), request.telefono(),
                request.nivelAcceso());
        AdministradorResponse usuario = administradorService.crear(crearReq);
        return registrar(usuario.id(), request.email(), request.contrasena(), NombreRol.ADMINISTRADOR);
    }

    private LoginResponse registrar(Long usuarioId, String email, String contrasena, NombreRol rol) {
        CuentaAcceso cuenta = cuentaAccesoService.crearCuenta(usuarioId, email, contrasena, rol);
        Sesion sesion = sesionService.crearSesion(cuenta.getId());
        String token = jwtTokenProvider.generarToken(
                cuenta.getUsuarioId(), cuenta.getRol().getNombre().name(), cuenta.getCredencial().getEmail());
        return new LoginResponse(token, sesion.getId(), cuenta.getUsuarioId(),
                cuenta.getCredencial().getEmail(), cuenta.getRol().getNombre().name());
    }
}
