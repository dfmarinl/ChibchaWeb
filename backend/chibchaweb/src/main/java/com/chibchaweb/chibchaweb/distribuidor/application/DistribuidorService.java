package com.chibchaweb.chibchaweb.distribuidor.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.distribuidor.domain.Distribuidor;
import com.chibchaweb.chibchaweb.distribuidor.domain.RegistroDistribuidorIntentoManager;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request.ActualizarDistribuidorRequest;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.request.CrearDistribuidorRequest;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.dto.response.DistribuidorResponse;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception.EmailDuplicadoException;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception.DistribuidorNoEncontradoException;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.exception.NombreDuplicadoException;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.mapper.DistribuidorDtoMapper;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence.DistribuidorDataMapper;
import com.chibchaweb.chibchaweb.distribuidor.infrastructure.persistence.DistribuidorJpaRepository;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;

@Service
@Transactional
public class DistribuidorService {

    private final DistribuidorDataMapper distribuidorMapper;
    private final DistribuidorJpaRepository distribuidorJpaRepository;
    private final DistribuidorDtoMapper dtoMapper;
    private final RegistroDistribuidorIntentoManager intentoManager;

    public DistribuidorService(DistribuidorDataMapper distribuidorMapper,
                               DistribuidorJpaRepository distribuidorJpaRepository,
                               DistribuidorDtoMapper dtoMapper,
                               RegistroDistribuidorIntentoManager intentoManager) {
        this.distribuidorMapper = distribuidorMapper;
        this.distribuidorJpaRepository = distribuidorJpaRepository;
        this.dtoMapper = dtoMapper;
        this.intentoManager = intentoManager;
    }

    public DistribuidorResponse crear(CrearDistribuidorRequest request) {
        var resultadoIntento = intentoManager.registrarIntento(request.email());
        if (resultadoIntento.limiteExcedido()) {
            throw new IntentoLimiteExcedidoException();
        }

        if (distribuidorJpaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicadoException(request.email(), resultadoIntento.intentosRestantes());
        }

        if (distribuidorJpaRepository.findByNombre(request.nombre()).isPresent()) {
            throw new NombreDuplicadoException(request.nombre(), resultadoIntento.intentosRestantes());
        }

        Distribuidor distribuidor = new Distribuidor(null, request.nombre(), request.email(),
                request.region(), null, request.maxDominios());
        var jpa = distribuidorMapper.toJpa(distribuidor);
        jpa.setCodigoDistribuidor("TEMP");
        jpa = distribuidorJpaRepository.save(jpa);
        String codigo = "DIST-" + jpa.getId();
        jpa.setCodigoDistribuidor(codigo);
        distribuidorJpaRepository.save(jpa);
        intentoManager.resetear(request.email());
        return dtoMapper.toResponse(distribuidorMapper.toDomain(jpa));
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

        if (request.nombre() != null) {
            distribuidorJpaRepository.findByNombre(request.nombre())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(e -> { throw new NombreDuplicadoException(request.nombre()); });
        }

        String nombre = request.nombre() != null ? request.nombre().trim() : distribuidor.getNombre();
        String email = request.email() != null ? request.email().trim().toLowerCase() : distribuidor.getEmail();
        String region = request.region() != null ? request.region().trim() : distribuidor.getRegion();
        int maxDominios = request.maxDominios() != null ? request.maxDominios() : distribuidor.getMaxDominios();

        Distribuidor merged = new Distribuidor(id, nombre, email, region, distribuidor.getCodigoDistribuidor(), maxDominios);
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
