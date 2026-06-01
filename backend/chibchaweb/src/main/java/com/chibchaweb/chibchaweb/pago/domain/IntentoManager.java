package com.chibchaweb.chibchaweb.pago.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class IntentoManager {

    private static final int MAX_INTENTOS = 4;
    private static final long TTL_MS = 30 * 60 * 1000;

    private final Map<Long, IntentoEntry> intentos = new ConcurrentHashMap<>();

    public ResultadoIntento registrarIntento(Long clienteId) {
        IntentoEntry entry = intentos.computeIfAbsent(clienteId, k -> new IntentoEntry());
        entry.limpiarSiExpirado();
        int actual = entry.incrementar();

        if (actual > MAX_INTENTOS) {
            return new ResultadoIntento(false, 0, true);
        }
        return new ResultadoIntento(true, MAX_INTENTOS - actual, false);
    }

    public void resetear(Long clienteId) {
        intentos.remove(clienteId);
    }

    public ResultadoIntento consultarIntentos(Long clienteId) {
        IntentoEntry entry = intentos.get(clienteId);
        if (entry == null) {
            return new ResultadoIntento(true, MAX_INTENTOS, false);
        }
        entry.limpiarSiExpirado();
        int actual = entry.getContador();
        if (actual > MAX_INTENTOS) {
            return new ResultadoIntento(false, 0, true);
        }
        return new ResultadoIntento(true, MAX_INTENTOS - actual, false);
    }

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

    public record ResultadoIntento(boolean permitido, int intentosRestantes, boolean limiteExcedido) {}
}
