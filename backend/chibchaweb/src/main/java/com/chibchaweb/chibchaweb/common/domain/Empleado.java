package com.chibchaweb.chibchaweb.common.domain;

import java.time.LocalDateTime;

public class Empleado extends Usuario {

    private String cargo;
    private String area;
    private LocalDateTime fechaContratacion;

    protected Empleado() {
    }

    public Empleado(Long id, String nombre, String email, String telefono,
                    String cargo, String area) {
        super(id, nombre, email, telefono);
        validarCargo(cargo);
        this.cargo = cargo.trim();
        this.area = (area != null) ? area.trim() : null;
        this.fechaContratacion = LocalDateTime.now();
    }

    private void validarCargo(String cargo) {
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("El cargo no puede estar vacio");
        }
    }

    public String getCargo() {
        return cargo;
    }

    public String getArea() {
        return area;
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

    public void cambiarArea(String nuevaArea) {
        this.area = (nuevaArea != null) ? nuevaArea.trim() : null;
    }

}
