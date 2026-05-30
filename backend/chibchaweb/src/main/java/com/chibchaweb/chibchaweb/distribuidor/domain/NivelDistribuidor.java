package com.chibchaweb.chibchaweb.distribuidor.domain;

public enum NivelDistribuidor {
    BASICO,
    PLATA,
    ORO,
    DIAMANTE,
    PREMIUM;

    public static NivelDistribuidor calcularNivel(int maxDominios) {
        if (maxDominios <= 100) return BASICO;
        return PREMIUM;
    }
}
