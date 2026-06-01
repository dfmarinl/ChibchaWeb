package com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;

@RestControllerAdvice("com.chibchaweb.chibchaweb.distribuidor.presentation")
public class DistribuidorExceptionHandler {

    @ExceptionHandler(DistribuidorNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleNoEncontrado(DistribuidorNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailDuplicado(EmailDuplicadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        if (ex.getIntentosRestantes() >= 0) {
            body.put("intentosRestantes", ex.getIntentosRestantes());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(NombreDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleNombreDuplicado(NombreDuplicadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        if (ex.getIntentosRestantes() >= 0) {
            body.put("intentosRestantes", ex.getIntentosRestantes());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(IntentoLimiteExcedidoException.class)
    public ResponseEntity<Map<String, Object>> handleIntentoLimiteExcedido(IntentoLimiteExcedidoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("limiteExcedido", true);
        return ResponseEntity.status(HttpStatus.LOCKED).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause().getMessage();
        if (message != null && message.contains("email")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El email ya esta registrado"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Violacion de integridad de datos"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Valor invalido",
                        (a, b) -> b));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", errors));
    }
}
