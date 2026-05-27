package com.chibchaweb.chibchaweb.pago.domain;

public class LuhnValidator {

    public static boolean validar(String numero) {
        if (numero == null) return false;
        String digitos = numero.replaceAll("\\s+", "");
        if (!digitos.matches("\\d{13,19}")) return false;

        int suma = 0;
        boolean alternar = false;
        for (int i = digitos.length() - 1; i >= 0; i--) {
            int digito = digitos.charAt(i) - '0';
            if (alternar) {
                digito *= 2;
                if (digito > 9) digito -= 9;
            }
            suma += digito;
            alternar = !alternar;
        }
        return suma % 10 == 0;
    }

    public static TipoTarjeta detectarTipo(String numero) {
        if (numero == null) return null;
        String n = numero.replaceAll("\\s+", "");
        if (n.startsWith("4") && (n.length() == 13 || n.length() == 16)) {
            return TipoTarjeta.VISA;
        }
        if (n.matches("^5[1-5].*") && n.length() == 16) {
            return TipoTarjeta.MASTERCARD;
        }
        if ((n.startsWith("300") || n.startsWith("301") || n.startsWith("302") ||
             n.startsWith("303") || n.startsWith("304") || n.startsWith("305") ||
             n.startsWith("36") || n.startsWith("38")) && n.length() == 14) {
            return TipoTarjeta.DINERS;
        }
        return null;
    }
}
