package com.chibchaweb.chibchaweb.usuario.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "usuario_id")
@DiscriminatorValue("EMPLEADO")
public class EmpleadoJpa extends UsuarioJpa {

    @Column(nullable = false)
    private String cargo;

    private String departamento;

    private double salario;

    @Column(name = "fecha_contratacion")
    private LocalDateTime fechaContratacion;

    public EmpleadoJpa() {
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public LocalDateTime getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDateTime fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }
}
