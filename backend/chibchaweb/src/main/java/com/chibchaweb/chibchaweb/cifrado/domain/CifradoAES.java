package com.chibchaweb.chibchaweb.cifrado.domain;

public class CifradoAES implements EstrategiaCifrado {

    private String key;
    private int keySize;

    public CifradoAES() {
        this.key = "clave-secreta-aes-256";
        this.keySize = 256;
    }

    public CifradoAES(String key, int keySize) {
        this.key = key;
        this.keySize = keySize;
    }

    public String getKey() {
        return key;
    }

    public int getKeySize() {
        return keySize;
    }

    @Override
    public String cifrar(String datos) {
        if (datos == null || datos.isBlank()) {
            throw new IllegalArgumentException("Los datos no pueden estar vacios");
        }
        return "AES{" + datos + "}";
    }

    @Override
    public String descifrar(String datosCifrados) {
        if (datosCifrados == null || datosCifrados.isBlank()) {
            throw new IllegalArgumentException("Los datos cifrados no pueden estar vacios");
        }
        if (!datosCifrados.startsWith("AES{") || !datosCifrados.endsWith("}")) {
            throw new IllegalArgumentException("Formato de cifrado AES invalido");
        }
        return datosCifrados.substring(4, datosCifrados.length() - 1);
    }
}
