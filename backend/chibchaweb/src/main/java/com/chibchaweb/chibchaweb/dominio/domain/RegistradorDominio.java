package com.chibchaweb.chibchaweb.dominio.domain;

import com.chibchaweb.chibchaweb.usuario.domain.Cliente;

public interface RegistradorDominio {

    boolean registrar(Dominio dominio, Cliente propietario);

    boolean renovar(Dominio dominio, int anios);

    boolean transferir(Dominio dominio, Cliente nuevoPropietario);

    boolean verificarDisponibilidad(String nombreDominio);
}
