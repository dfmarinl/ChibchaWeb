package com.chibchaweb.chibchaweb.dominio.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.dominio.domain.SitioWeb;
import com.chibchaweb.chibchaweb.dominio.infrastructure.dto.SitioWebRequest;
import com.chibchaweb.chibchaweb.dominio.infrastructure.dto.SitioWebResponse;
import com.chibchaweb.chibchaweb.dominio.infrastructure.persistence.SitioWebDataMapper;
import com.chibchaweb.chibchaweb.dominio.infrastructure.persistence.SitioWebJpa;
import com.chibchaweb.chibchaweb.dominio.infrastructure.persistence.SitioWebJpaRepository;
import com.chibchaweb.chibchaweb.pago.domain.EstadoSuscripcion;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.SuscripcionDataMapper;
import com.chibchaweb.chibchaweb.pago.infrastructure.persistence.SuscripcionJpaRepository;
import com.chibchaweb.chibchaweb.plan.domain.HostingPlan;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanDataMapper;
import com.chibchaweb.chibchaweb.usuario.domain.Cliente;
import com.chibchaweb.chibchaweb.usuario.infrastructure.exception.UsuarioNoEncontradoException;
import com.chibchaweb.chibchaweb.usuario.infrastructure.persistence.ClienteDataMapper;

@Service
@Transactional
public class SitioWebService {

    private final SitioWebDataMapper sitioWebMapper;
    private final SitioWebJpaRepository sitioWebJpaRepository;
    private final ClienteDataMapper clienteMapper;
    private final SuscripcionJpaRepository suscripcionRepository;
    private final HostingPlanDataMapper planMapper;
    private final SuscripcionDataMapper suscripcionMapper;

    public SitioWebService(SitioWebDataMapper sitioWebMapper,
                           SitioWebJpaRepository sitioWebJpaRepository,
                           ClienteDataMapper clienteMapper,
                           SuscripcionJpaRepository suscripcionRepository,
                           HostingPlanDataMapper planMapper,
                           SuscripcionDataMapper suscripcionMapper) {
        this.sitioWebMapper = sitioWebMapper;
        this.sitioWebJpaRepository = sitioWebJpaRepository;
        this.clienteMapper = clienteMapper;
        this.suscripcionRepository = suscripcionRepository;
        this.planMapper = planMapper;
        this.suscripcionMapper = suscripcionMapper;
    }

    public SitioWebResponse crear(Long clienteId, SitioWebRequest request) {
        Cliente propietario = clienteMapper.findById(clienteId);
        if (propietario == null) throw UsuarioNoEncontradoException.porId(clienteId);

        var suscripcionJpa = suscripcionRepository
                .findFirstByClienteIdAndEstado(clienteId, EstadoSuscripcion.ACTIVA)
                .orElseThrow(() -> new IllegalStateException(
                    "No tienes una suscripcion activa para crear sitios web"));

        HostingPlan plan = planMapper.toDomain(suscripcionJpa.getPlanHosting());
        if (plan == null) throw new IllegalStateException("El plan de la suscripcion no existe");

        int sitiosActuales = sitioWebJpaRepository.findByPropietarioId(clienteId).size();
        if (sitiosActuales >= plan.getLimiteSitios()) {
            throw new IllegalStateException(
                "Has alcanzado el limite de " + plan.getLimiteSitios()
                    + " sitios para tu plan " + plan.getNombre());
        }

        String url = request.urlSitio().toLowerCase().trim() + ".chibchaweb.com";
        SitioWeb sitio = new SitioWeb(null, url, propietario);
        sitio.asignarSuscripcion(suscripcionMapper.toDomain(suscripcionJpa));

        var saved = sitioWebJpaRepository.save(sitioWebMapper.toJpa(sitio));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SitioWebResponse> listarPorCliente(Long clienteId) {
        return sitioWebJpaRepository.findByPropietarioId(clienteId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void eliminar(Long id, Long clienteId) {
        SitioWeb sitio = sitioWebMapper.findById(id);
        if (sitio == null) throw new RuntimeException("Sitio web no encontrado");
        if (!sitio.getPropietario().getId().equals(clienteId)) {
            throw new RuntimeException("El sitio web no pertenece al cliente");
        }
        sitioWebMapper.delete(id);
    }

    private SitioWebResponse toResponse(SitioWebJpa jpa) {
        return new SitioWebResponse(
            jpa.getId(),
            jpa.getUrlSitio(),
            jpa.isEstadoActivo(),
            jpa.getFechaCreacion()
        );
    }
}
