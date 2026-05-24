package com.chibchaweb.chibchaweb.cifrado.domain;

public class CifradoRSA implements EstrategiaCifrado {

    private String publicKey;
    private String privateKey;

    public CifradoRSA() {
        this.publicKey = "pub-key";
        this.privateKey = "priv-key";
    }

    public CifradoRSA(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public String cifrar(String datos) {
        if (datos == null || datos.isBlank()) {
            throw new IllegalArgumentException("Los datos no pueden estar vacios");
        }
        return "RSA{" + datos + "}";
    }

    @Override
    public String descifrar(String datosCifrados) {
        if (datosCifrados == null || datosCifrados.isBlank()) {
            throw new IllegalArgumentException("Los datos cifrados no pueden estar vacios");
        }
        if (!datosCifrados.startsWith("RSA{") || !datosCifrados.endsWith("}")) {
            throw new IllegalArgumentException("Formato de cifrado RSA invalido");
        }
        return datosCifrados.substring(4, datosCifrados.length() - 1);
    }
}
