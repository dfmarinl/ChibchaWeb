package com.chibchaweb.chibchaweb.usuario.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class RegistroIntentoManager {

    private static final int MAX_INTENTOS = 3;
    private static final long TTL_MS = 30 * 60 * 1000;

    private final Map<String, IntentoEntry> intentos = new ConcurrentHashMap<>();

    public ResultadoIntento registrarIntento(String email) {
        IntentoEntry entry = intentos.computeIfAbsent(email, k -> new IntentoEntry());
        entry.limpiarSiExpirado();
        int actual = entry.incrementar();

        if (actual > MAX_INTENTOS) {
            return new ResultadoIntento(false, 0, true);
        }
        return new ResultadoIntento(true, MAX_INTENTOS - actual, false);
    }

    public void resetear(String email) {
        intentos.remove(email);
    }

    public record ResultadoIntento(boolean permitido, int intentosRestantes, boolean limiteExcedido) {}

    private static class IntentoEntry {
        private final AtomicInteger contador = new AtomicInteger(0);
        private long ultimoIntento = System.currentTimeMillis();

        public int incrementar() {
            ultimoIntento = System.currentTimeMillis();
            return contador.incrementAndGet();
        }

        public int getContador() {
            return contador.get();
        }

        public void limpiarSiExpirado() {
            if (System.currentTimeMillis() - ultimoIntento > TTL_MS) {
                contador.set(0);
            }
        }
    }
}
