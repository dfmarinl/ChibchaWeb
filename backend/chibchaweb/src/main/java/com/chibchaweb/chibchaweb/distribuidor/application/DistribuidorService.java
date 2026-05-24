package com.chibchaweb.chibchaweb.distribuidor.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.distribuidor.domain.Distribuidor;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request.ActualizarDistribuidorRequest;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request.CrearDistribuidorRequest;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.response.DistribuidorResponse;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception.EmailDuplicadoException;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception.DistribuidorNoEncontradoException;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.mapper.DistribuidorDtoMapper;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence.DistribuidorJpa;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence.DistribuidorJpaRepository;

@Service
@Transactional
public class DistribuidorService {

    private final DistribuidorJpaRepository jpaRepository;
    private final DistribuidorDtoMapper dtoMapper;

    public DistribuidorService(DistribuidorJpaRepository jpaRepository,
                               DistribuidorDtoMapper dtoMapper) {
        this.jpaRepository = jpaRepository;
        this.dtoMapper = dtoMapper;
    }

    public DistribuidorResponse crear(CrearDistribuidorRequest request) {
        if (jpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email());
        }
        Distribuidor distribuidor = new Distribuidor(null, request.nombre(), request.email(),
                request.region(), request.codigoDistribuidor(), request.nivelDistribuidor());
        DistribuidorJpa jpa = toJpa(distribuidor);
        DistribuidorJpa saved = jpaRepository.save(jpa);
        return dtoMapper.toResponse(toDomain(saved));
    }

    @Transactional(readOnly = true)
    public DistribuidorResponse buscarPorId(Long id) {
        DistribuidorJpa jpa = jpaRepository.findById(id)
                .orElseThrow(() -> DistribuidorNoEncontradoException.porId(id));
        return dtoMapper.toResponse(toDomain(jpa));
    }

    @Transactional(readOnly = true)
    public List<DistribuidorResponse> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .map(dtoMapper::toResponse)
                .toList();
    }

    public DistribuidorResponse actualizar(Long id, ActualizarDistribuidorRequest request) {
        DistribuidorJpa jpa = jpaRepository.findById(id)
                .orElseThrow(() -> DistribuidorNoEncontradoException.porId(id));
        if (request.nombre() != null) jpa.setNombre(request.nombre().trim());
        if (request.email() != null) {
            jpaRepository.findByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new EmailDuplicadoException(request.email()); });
            jpa.setEmail(request.email().trim().toLowerCase());
        }
        if (request.region() != null) jpa.setRegion(request.region().trim());
        if (request.codigoDistribuidor() != null) jpa.setCodigoDistribuidor(request.codigoDistribuidor().trim());
        if (request.nivelDistribuidor() != null) jpa.setNivelDistribuidor(request.nivelDistribuidor());
        DistribuidorJpa saved = jpaRepository.save(jpa);
        return dtoMapper.toResponse(toDomain(saved));
    }

    public void eliminar(Long id) {
        if (!jpaRepository.existsById(id)) {
            throw DistribuidorNoEncontradoException.porId(id);
        }
        jpaRepository.deleteById(id);
    }

    private DistribuidorJpa toJpa(Distribuidor domain) {
        if (domain == null) return null;
        DistribuidorJpa jpa = new DistribuidorJpa();
        jpa.setId(domain.getId());
        jpa.setNombre(domain.getNombre());
        jpa.setEmail(domain.getEmail());
        jpa.setRegion(domain.getRegion());
        jpa.setCodigoDistribuidor(domain.getCodigoDistribuidor());
        jpa.setNivelDistribuidor(domain.getNivelDistribuidor() != null
                ? domain.getNivelDistribuidor()
                : com.chibchaweb.chibchaweb.distribuidor.domain.NivelDistribuidor.BASICO);
        return jpa;
    }

    private Distribuidor toDomain(DistribuidorJpa jpa) {
        if (jpa == null) return null;
        return new Distribuidor(
                jpa.getId(), jpa.getNombre(), jpa.getEmail(), jpa.getRegion(),
                jpa.getCodigoDistribuidor(), jpa.getNivelDistribuidor());
    }
}
