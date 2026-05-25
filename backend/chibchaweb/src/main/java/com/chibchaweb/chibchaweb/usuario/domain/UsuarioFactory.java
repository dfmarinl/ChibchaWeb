package com.chibchaweb.chibchaweb.usuario.domain;

import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearAdministradorRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearClienteRequest;
import com.chibchaweb.chibchaweb.usuario.infrastructure.dto.request.CrearEmpleadoRequest;

@Component
public class UsuarioFactory {

    public Cliente crearUsuario(NombreRol rol, CrearClienteRequest request) {
        validarRol(rol, NombreRol.CLIENTE);
        return new Cliente(null, request.nombre(), request.email(), request.telefono(),
                request.direccion(), request.documentoIdentidad(), request.region());
    }

    public Empleado crearUsuario(NombreRol rol, CrearEmpleadoRequest request) {
        validarRol(rol, NombreRol.EMPLEADO);
        return new Empleado(null, request.nombre(), request.email(), request.telefono(),
                request.cargo(), request.departamento(), request.salario());
    }

    public Administrador crearUsuario(NombreRol rol, CrearAdministradorRequest request) {
        validarRol(rol, NombreRol.ADMINISTRADOR);
        return new Administrador(null, request.nombre(), request.email(), request.telefono(),
                request.nivelAcceso());
    }

    private void validarRol(NombreRol esperado, NombreRol actual) {
        if (esperado != actual) {
            throw new IllegalArgumentException(
                    "El rol " + actual + " no coincide con el tipo de solicitud");
        }
    }
}
