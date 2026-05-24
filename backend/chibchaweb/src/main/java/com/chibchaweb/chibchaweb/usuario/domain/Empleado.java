package com.chibchaweb.chibchaweb.usuario.domain;

import java.time.LocalDateTime;

public class Empleado extends Usuario {

    private String cargo;
    private String departamento;
    private double salario;
    private LocalDateTime fechaContratacion;

    public Empleado() {
    }

    public Empleado(Long id, String nombre, String email, String telefono,
                    String cargo, String departamento, double salario) {
        super(id, nombre, email, telefono);
        validarCargo(cargo);
        this.cargo = cargo.trim();
        this.departamento = (departamento != null) ? departamento.trim() : null;
        this.salario = salario;
        this.fechaContratacion = LocalDateTime.now();
    }

    private void validarCargo(String cargo) {
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("El cargo no puede estar vacio");
        }
    }

    @Override
    public String getTipoUsuario() {
        return "EMPLEADO";
    }

    public String getCargo() {
        return cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public double getSalario() {
        return salario;
    }

    public LocalDateTime getFechaContratacion() {
        return fechaContratacion;
    }

    public void cambiarCargo(String nuevoCargo) {
        if (nuevoCargo == null || nuevoCargo.isBlank()) {
            throw new IllegalArgumentException("El cargo no puede estar vacio");
        }
        this.cargo = nuevoCargo.trim();
    }

    public void cambiarDepartamento(String nuevoDepartamento) {
        this.departamento = (nuevoDepartamento != null) ? nuevoDepartamento.trim() : null;
    }

    public void asignarSalario(double nuevoSalario) {
        if (nuevoSalario < 0) {
            throw new IllegalArgumentException("El salario no puede ser negativo");
        }
        this.salario = nuevoSalario;
    }
}
