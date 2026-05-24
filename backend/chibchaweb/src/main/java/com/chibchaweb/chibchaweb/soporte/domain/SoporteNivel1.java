package com.chibchaweb.chibchaweb.soporte.domain;

import com.chibchaweb.chibchaweb.usuario.domain.Empleado;

public class SoporteNivel1 extends ManejadorSoporte {

    private Empleado tecnico;

    protected SoporteNivel1() {
    }

    public SoporteNivel1(int nivelSoporte, Empleado tecnico) {
        super(nivelSoporte);
        this.tecnico = tecnico;
    }

    public Empleado getTecnico() {
        return tecnico;
    }

    @Override
    public boolean manejar(Ticket ticket) {
        if (ticket.getNivel() <= nivelSoporte) {
            resolverProblemaBasico(ticket);
            return true;
        }
        return pasarAlSiguiente(ticket);
    }

    public void resolverProblemaBasico(Ticket ticket) {
        ticket.procesar();
        ticket.agregarComentario("Resuelto por soporte nivel 1: " + tecnico.getNombre());
        ticket.resolver();
        ticket.cerrar();
    }
}
