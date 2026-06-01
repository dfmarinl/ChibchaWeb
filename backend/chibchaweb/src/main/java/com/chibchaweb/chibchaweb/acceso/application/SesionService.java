package com.chibchaweb.chibchaweb.acceso.application;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.Sesion;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.SesionJpa;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.SesionJpaRepository;
import com.chibchaweb.chibchaweb.acceso.infrastructure.persistence.SesionMapper;

@Service
public class SesionService {

    private static final int DURACION_MINUTOS = 60;

    private final SesionMapper mapper;
    private final SesionJpaRepository repository;

    public SesionService(SesionMapper mapper, SesionJpaRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Transactional
    public Sesion crearSesion(Long cuentaId) {
        invalidarSesionesActivas(cuentaId);
        Sesion sesion = new Sesion(null, cuentaId, DURACION_MINUTOS);
        SesionJpa jpa = mapper.toJpa(sesion);
        SesionJpa saved = repository.save(jpa);
        return mapper.toDomain(saved);
    }

    @Transactional
    public void invalidarSesion(Long sesionId) {
        Sesion sesion = mapper.findById(sesionId);
        if (sesion != null) {
            sesion.invalidar();
            mapper.update(sesion);
        }
    }

    @Transactional
    public void invalidarSesionesActivas(Long cuentaId) {
        List<SesionJpa> activas = repository.findByCuentaIdAndActivaTrue(cuentaId);
        for (SesionJpa jpa : activas) {
            Sesion sesion = mapper.toDomain(jpa);
            sesion.invalidar();
            mapper.update(sesion);
        }
    }

    public Optional<Sesion> validarSesion(Long sesionId) {
        Sesion sesion = mapper.findById(sesionId);
        if (sesion != null && sesion.estaVigente()) {
            return Optional.of(sesion);
        }
        return Optional.empty();
    }
}
