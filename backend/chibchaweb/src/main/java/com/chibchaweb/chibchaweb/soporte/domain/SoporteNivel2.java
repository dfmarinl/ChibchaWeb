package com.chibchaweb.chibchaweb.soporte.domain;

import com.chibchaweb.chibchaweb.usuario.domain.Empleado;

public class SoporteNivel2 extends ManejadorSoporte {

    private Empleado especialista;

    protected SoporteNivel2() {
    }

    public SoporteNivel2(int nivelSoporte, Empleado especialista) {
        super(nivelSoporte);
        this.especialista = especialista;
    }

    public Empleado getEspecialista() {
        return especialista;
    }

    @Override
    public boolean manejar(Ticket ticket) {
        if (ticket.getNivel() <= nivelSoporte) {
            analizarProblema(ticket);
            return true;
        }
        return pasarAlSiguiente(ticket);
    }

    public void analizarProblema(Ticket ticket) {
        ticket.procesar();
        ticket.agregarComentario("Analizado por soporte nivel 2: " + especialista.getNombre());
        ticket.resolver();
        ticket.cerrar();
    }
}
