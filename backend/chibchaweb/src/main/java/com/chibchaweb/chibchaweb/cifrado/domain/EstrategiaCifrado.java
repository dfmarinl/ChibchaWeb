package com.chibchaweb.chibchaweb.cifrado.domain;

public interface EstrategiaCifrado {

    String cifrar(String datos);

    String descifrar(String datosCifrados);
}
