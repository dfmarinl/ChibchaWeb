package com.chibchaweb.chibchaweb.cifrado.domain;

public class GestorCifrado {

    private EstrategiaCifrado estrategia;

    public GestorCifrado() {
    }

    public GestorCifrado(EstrategiaCifrado estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de cifrado no puede ser nula");
        }
        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaCifrado estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de cifrado no puede ser nula");
        }
        this.estrategia = estrategia;
    }

    public EstrategiaCifrado getEstrategia() {
        return estrategia;
    }

    public String cifrar(String datos) {
        if (estrategia == null) {
            throw new IllegalStateException("No hay estrategia de cifrado configurada");
        }
        return estrategia.cifrar(datos);
    }

    public String descifrar(String datosCifrados) {
        if (estrategia == null) {
            throw new IllegalStateException("No hay estrategia de cifrado configurada");
        }
        return estrategia.descifrar(datosCifrados);
    }
}
