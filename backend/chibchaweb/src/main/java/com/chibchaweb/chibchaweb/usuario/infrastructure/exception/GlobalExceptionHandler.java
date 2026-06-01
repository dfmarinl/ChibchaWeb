package com.chibchaweb.chibchaweb.usuario.infrastructure.exception;

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

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
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

    @ExceptionHandler(DocumentoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleDocumentoDuplicado(DocumentoDuplicadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        if (ex.getIntentosRestantes() >= 0) {
            body.put("intentosRestantes", ex.getIntentosRestantes());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(TipoUsuarioInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleTipoUsuarioInvalido(TipoUsuarioInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
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
        if (message != null && message.contains("documento_identidad")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El documento de identidad ya esta registrado"));
        }
        if (message != null && message.contains("sitio_web")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "No se puede eliminar el cliente porque tiene sitios web asociados"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Violacion de integridad de datos"));
    }

    @ExceptionHandler(IntentoLimiteExcedidoException.class)
    public ResponseEntity<Map<String, Object>> handleIntentoLimiteExcedido(IntentoLimiteExcedidoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("limiteExcedido", true);
        return ResponseEntity.status(HttpStatus.LOCKED).body(body);
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
