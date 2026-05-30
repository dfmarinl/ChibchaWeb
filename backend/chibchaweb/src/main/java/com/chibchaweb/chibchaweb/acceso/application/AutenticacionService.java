package com.chibchaweb.chibchaweb.acceso.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.CuentaAcceso;
import com.chibchaweb.chibchaweb.acceso.domain.PasswordEncoder;
import com.chibchaweb.chibchaweb.acceso.domain.Sesion;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.CuentaAccesoJpaRepository;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.CuentaAccesoMapper;
import com.chibchaweb.chibchaweb.acceso.infrastructure.security.JwtTokenProvider;

@Service
public class AutenticacionService {

    private final CuentaAccesoMapper cuentaAccesoMapper;
    private final CuentaAccesoJpaRepository cuentaAccesoJpaRepository;
    private final SesionService sesionService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AutenticacionService(CuentaAccesoMapper cuentaAccesoMapper,
                                CuentaAccesoJpaRepository cuentaAccesoJpaRepository,
                                SesionService sesionService,
                                PasswordEncoder passwordEncoder,
                                JwtTokenProvider jwtTokenProvider) {
        this.cuentaAccesoMapper = cuentaAccesoMapper;
        this.cuentaAccesoJpaRepository = cuentaAccesoJpaRepository;
        this.sesionService = sesionService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public LoginResultado login(String email, String contrasena) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("La contrasena no puede estar vacia");
        }

        var jpaOpt = cuentaAccesoJpaRepository.findByCredencialEmail(email.trim().toLowerCase());
        if (jpaOpt.isEmpty()) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        CuentaAcceso cuenta = cuentaAccesoMapper.toDomain(jpaOpt.get());
        boolean autenticado = cuenta.autenticar(contrasena, passwordEncoder);
        cuentaAccesoMapper.update(cuenta);

        if (!autenticado) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        Sesion sesion = sesionService.crearSesion(cuenta.getId());
        String token = jwtTokenProvider.generarToken(
                cuenta.getCredencial().getEmail(),
                cuenta.getRol().getNombre().name());

        return new LoginResultado(token, sesion.getId(), cuenta);
    }

    public record LoginResultado(
            String token,
            Long sesionId,
            String email,
            String rol
    ) {
        LoginResultado(String token, Long sesionId, CuentaAcceso cuenta) {
            this(token, sesionId, cuenta.getCredencial().getEmail(), cuenta.getRol().getNombre().name());
        }
    }
}
