package com.chibchaweb.chibchaweb.soporte.domain;

import com.chibchaweb.chibchaweb.usuario.domain.Empleado;

public class SoporteNivel3 extends ManejadorSoporte {

    private Empleado ingeniero;

    protected SoporteNivel3() {
    }

    public SoporteNivel3(int nivelSoporte, Empleado ingeniero) {
        super(nivelSoporte);
        this.ingeniero = ingeniero;
    }

    public Empleado getIngeniero() {
        return ingeniero;
    }

    @Override
    public boolean manejar(Ticket ticket) {
        if (ticket.getNivel() <= nivelSoporte) {
            escalarAProveedor(ticket);
            return true;
        }
        return pasarAlSiguiente(ticket);
    }

    public void escalarAProveedor(Ticket ticket) {
        ticket.procesar();
        ticket.agregarComentario("Escalado a proveedor externo por: " + ingeniero.getNombre());
        ticket.resolver();
        ticket.cerrar();
    }
}
