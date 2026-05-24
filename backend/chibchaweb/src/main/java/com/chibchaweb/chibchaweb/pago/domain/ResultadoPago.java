package com.chibchaweb.chibchaweb.pago.domain;

public class ResultadoPago {

    private boolean exitoso;
    private String codigoRespuesta;
    private String mensaje;
    private String transaccionId;

    public ResultadoPago() {
    }

    public ResultadoPago(boolean exitoso, String codigoRespuesta, String mensaje, String transaccionId) {
        this.exitoso = exitoso;
        this.codigoRespuesta = codigoRespuesta;
        this.mensaje = mensaje;
        this.transaccionId = transaccionId;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    @Override
    public String toString() {
        return "ResultadoPago{exitoso=" + exitoso + ", codigo='" + codigoRespuesta + "', transaccion='" + transaccionId + "'}";
    }
}
