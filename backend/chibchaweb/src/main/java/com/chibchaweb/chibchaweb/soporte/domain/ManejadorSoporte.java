package com.chibchaweb.chibchaweb.soporte.domain;

public abstract class ManejadorSoporte {

    protected int nivelSoporte;
    protected ManejadorSoporte siguienteManejador;

    protected ManejadorSoporte() {
    }

    protected ManejadorSoporte(int nivelSoporte) {
        this.nivelSoporte = nivelSoporte;
    }

    public abstract boolean manejar(Ticket ticket);

    public ManejadorSoporte setSiguiente(ManejadorSoporte siguiente) {
        this.siguienteManejador = siguiente;
        return siguiente;
    }

    protected boolean pasarAlSiguiente(Ticket ticket) {
        if (siguienteManejador != null) {
            return siguienteManejador.manejar(ticket);
        }
        return false;
    }

    public int getNivelSoporte() {
        return nivelSoporte;
    }

    public ManejadorSoporte getSiguienteManejador() {
        return siguienteManejador;
    }
}
