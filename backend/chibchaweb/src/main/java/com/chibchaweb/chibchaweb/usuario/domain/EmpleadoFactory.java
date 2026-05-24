package com.chibchaweb.chibchaweb.usuario.domain;

import org.springframework.stereotype.Component;

@Component
public class EmpleadoFactory extends UsuarioFactory {

    @Override
    public Empleado crearUsuario(Long id, String nombre, String email, String telefono) {
        throw new UnsupportedOperationException(
            "Use crearEmpleado(id, nombre, email, telefono, cargo, departamento, salario)");
    }

    public Empleado crearEmpleado(Long id, String nombre, String email, String telefono,
                                   String cargo, String departamento, double salario) {
        return new Empleado(id, nombre, email, telefono, cargo, departamento, salario);
    }
}
