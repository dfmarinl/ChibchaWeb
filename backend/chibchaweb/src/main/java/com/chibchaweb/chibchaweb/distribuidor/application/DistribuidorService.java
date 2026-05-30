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
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence.DistribuidorDataMapper;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence.DistribuidorJpaRepository;

@Service
@Transactional
public class DistribuidorService {

    private final DistribuidorDataMapper distribuidorMapper;
    private final DistribuidorJpaRepository distribuidorJpaRepository;
    private final DistribuidorDtoMapper dtoMapper;

    public DistribuidorService(DistribuidorDataMapper distribuidorMapper,
                               DistribuidorJpaRepository distribuidorJpaRepository,
                               DistribuidorDtoMapper dtoMapper) {
        this.distribuidorMapper = distribuidorMapper;
        this.distribuidorJpaRepository = distribuidorJpaRepository;
        this.dtoMapper = dtoMapper;
    }

    public DistribuidorResponse crear(CrearDistribuidorRequest request) {
        if (distribuidorJpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email());
        }
        Distribuidor distribuidor = new Distribuidor(null, request.nombre(), request.email(),
                request.region(), request.codigoDistribuidor(), request.maxDominios());
        var saved = distribuidorJpaRepository.save(distribuidorMapper.toJpa(distribuidor));
        return dtoMapper.toResponse(distribuidorMapper.toDomain(saved));
    }

    @Transactional(readOnly = true)
    public DistribuidorResponse buscarPorId(Long id) {
        Distribuidor distribuidor = distribuidorMapper.findById(id);
        if (distribuidor == null) throw DistribuidorNoEncontradoException.porId(id);
        return dtoMapper.toResponse(distribuidor);
    }

    @Transactional(readOnly = true)
    public List<DistribuidorResponse> listarTodos() {
        return distribuidorMapper.findAll().stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

    public DistribuidorResponse actualizar(Long id, ActualizarDistribuidorRequest request) {
        Distribuidor distribuidor = distribuidorMapper.findById(id);
        if (distribuidor == null) throw DistribuidorNoEncontradoException.porId(id);

        if (request.email() != null) {
            distribuidorJpaRepository.findByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new EmailDuplicadoException(request.email()); });
        }

        String nombre = request.nombre() != null ? request.nombre().trim() : distribuidor.getNombre();
        String email = request.email() != null ? request.email().trim().toLowerCase() : distribuidor.getEmail();
        String region = request.region() != null ? request.region().trim() : distribuidor.getRegion();
        String codigo = request.codigoDistribuidor() != null ? request.codigoDistribuidor().trim() : distribuidor.getCodigoDistribuidor();
        int maxDominios = request.maxDominios() != null ? request.maxDominios() : distribuidor.getMaxDominios();

        Distribuidor merged = new Distribuidor(id, nombre, email, region, codigo, maxDominios);
        distribuidorMapper.update(merged);
        Distribuidor actualizado = distribuidorMapper.findById(id);
        return dtoMapper.toResponse(actualizado);
    }

    public void eliminar(Long id) {
        Distribuidor distribuidor = distribuidorMapper.findById(id);
        if (distribuidor == null) throw DistribuidorNoEncontradoException.porId(id);
        distribuidorMapper.delete(id);
    }
}
