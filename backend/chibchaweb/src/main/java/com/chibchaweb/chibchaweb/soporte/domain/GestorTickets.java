package com.chibchaweb.chibchaweb.soporte.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorTickets {

    private List<Ticket> ticketsAbiertos;
    private ManejadorSoporte cadena;

    public GestorTickets() {
        this.ticketsAbiertos = new ArrayList<>();
    }

    public GestorTickets(ManejadorSoporte cadena) {
        this();
        this.cadena = cadena;
    }

    public void setCadena(ManejadorSoporte cadena) {
        if (cadena == null) {
            throw new IllegalArgumentException("La cadena de soporte no puede ser nula");
        }
        this.cadena = cadena;
    }

    public ManejadorSoporte getCadena() {
        return cadena;
    }

    public List<Ticket> getTicketsAbiertos() {
        return Collections.unmodifiableList(ticketsAbiertos);
    }

    public void registrarTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("El ticket no puede ser nulo");
        }
        ticketsAbiertos.add(ticket);
    }

    public void procesarTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("El ticket no puede ser nulo");
        }
        if (cadena == null) {
            throw new IllegalStateException("No hay cadena de soporte configurada");
        }
        cadena.manejar(ticket);
        ticketsAbiertos.remove(ticket);
    }

    public void procesarTodos() {
        List<Ticket> pendientes = new ArrayList<>(ticketsAbiertos);
        for (Ticket ticket : pendientes) {
            procesarTicket(ticket);
        }
    }
}
