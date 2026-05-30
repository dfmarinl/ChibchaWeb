package com.chibchaweb.chibchaweb.acceso.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.CuentaAcceso;
import com.chibchaweb.chibchaweb.acceso.domain.Credencial;
import com.chibchaweb.chibchaweb.acceso.domain.EstadoCuenta;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;
import com.chibchaweb.chibchaweb.acceso.domain.PasswordEncoder;
import com.chibchaweb.chibchaweb.acceso.domain.Rol;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.CredencialJpa;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.CredencialJpaRepository;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.CuentaAccesoJpa;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.CuentaAccesoJpaRepository;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.RolJpa;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.RolJpaRepository;

@Service
@Transactional
public class CuentaAccesoService {

    private final CredencialJpaRepository credencialJpaRepository;
    private final CuentaAccesoJpaRepository cuentaAccesoJpaRepository;
    private final RolJpaRepository rolJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public CuentaAccesoService(CredencialJpaRepository credencialJpaRepository,
                                CuentaAccesoJpaRepository cuentaAccesoJpaRepository,
                                RolJpaRepository rolJpaRepository,
                                PasswordEncoder passwordEncoder) {
        this.credencialJpaRepository = credencialJpaRepository;
        this.cuentaAccesoJpaRepository = cuentaAccesoJpaRepository;
        this.rolJpaRepository = rolJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CuentaAcceso crearCuenta(String email, String rawPassword, NombreRol nombreRol) {
        String hash = passwordEncoder.encode(rawPassword);

        CredencialJpa credencialJpa = new CredencialJpa();
        credencialJpa.setEmail(email.trim().toLowerCase());
        credencialJpa.setHashContrasena(hash);
        credencialJpa.setIntentosFallidos(0);
        credencialJpa = credencialJpaRepository.save(credencialJpa);

        RolJpa rolJpa = rolJpaRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + nombreRol));

        CuentaAccesoJpa cuentaJpa = new CuentaAccesoJpa();
        cuentaJpa.setEstado(EstadoCuenta.ACTIVA);
        cuentaJpa.setRol(rolJpa);
        cuentaJpa.setCredencial(credencialJpa);
        cuentaJpa = cuentaAccesoJpaRepository.save(cuentaJpa);

        Credencial credencial = new Credencial(credencialJpa.getId(), email, hash);
        Rol rol = new Rol(rolJpa.getId(), nombreRol);
        return new CuentaAcceso(cuentaJpa.getId(), rol, credencial);
    }
}
