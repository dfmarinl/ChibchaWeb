package com.chibchaweb.chibchaweb.cifrado.domain;

public class CifradoDES implements EstrategiaCifrado {

    private String key;

    public CifradoDES() {
        this.key = "des-key";
    }

    public CifradoDES(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String cifrar(String datos) {
        if (datos == null || datos.isBlank()) {
            throw new IllegalArgumentException("Los datos no pueden estar vacios");
        }
        return "DES{" + datos + "}";
    }

    @Override
    public String descifrar(String datosCifrados) {
        if (datosCifrados == null || datosCifrados.isBlank()) {
            throw new IllegalArgumentException("Los datos cifrados no pueden estar vacios");
        }
        if (!datosCifrados.startsWith("DES{") || !datosCifrados.endsWith("}")) {
            throw new IllegalArgumentException("Formato de cifrado DES invalido");
        }
        return datosCifrados.substring(4, datosCifrados.length() - 1);
    }
}
