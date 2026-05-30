package com.chibchaweb.chibchaweb.plan.application;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.pago.domain.IntentoManager;
import com.chibchaweb.chibchaweb.pago.domain.IntentoManager.ResultadoIntento;
import com.chibchaweb.chibchaweb.pago.infrastructure.exception.IntentoLimiteExcedidoException;
import com.chibchaweb.chibchaweb.plan.domain.HostingPlan;
import com.chibchaweb.chibchaweb.plan.domain.PlanOroUnix;
import com.chibchaweb.chibchaweb.plan.domain.PlanOroWindows;
import com.chibchaweb.chibchaweb.plan.domain.PlanPlataUnix;
import com.chibchaweb.chibchaweb.plan.domain.PlanPlataWindows;
import com.chibchaweb.chibchaweb.plan.domain.PlanPlatinoUnix;
import com.chibchaweb.chibchaweb.plan.domain.PlanPlatinoWindows;
import com.chibchaweb.chibchaweb.plan.infrastructure.dto.AsociarPlataformaRequest;
import com.chibchaweb.chibchaweb.plan.infrastructure.dto.CrearPlanRequest;
import com.chibchaweb.chibchaweb.plan.infrastructure.dto.ModificarPlanRequest;
import com.chibchaweb.chibchaweb.plan.infrastructure.dto.PlanHostingResponse;
import com.chibchaweb.chibchaweb.plan.infrastructure.exception.PlanNoEncontradoException;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanDataMapper;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanJpa;
import com.chibchaweb.chibchaweb.plan.infrastructure.persistence.HostingPlanJpaRepository;

@Service
@Transactional
public class HostingPlanService {

    private final HostingPlanDataMapper planMapper;
    private final HostingPlanJpaRepository planRepository;
    private final IntentoManager intentoManager;

    public HostingPlanService(HostingPlanDataMapper planMapper,
                              HostingPlanJpaRepository planRepository,
                              IntentoManager intentoManager) {
        this.planMapper = planMapper;
        this.planRepository = planRepository;
        this.intentoManager = intentoManager;
    }

    public PlanHostingResponse crear(Long clienteId, CrearPlanRequest request) {
        if (clienteId != null) {
            ResultadoIntento resultado = intentoManager.registrarIntento(clienteId);
            if (resultado.limiteExcedido()) {
                throw new IntentoLimiteExcedidoException();
            }
        }

        HostingPlan plan = crearDomain(request.tipoPlan(), request.plataforma(),
                null, request.nombre(), request.precioMensual(),
                request.espacioDisco(), request.anchoBanda(), request.cuentasEmail(),
                request.mysqlIncluido(), request.phpVersion(),
                request.sqlServerIncluido(), request.iisVersion(),
                request.pythonIncluido(), request.aspNetVersion());

        var saved = planRepository.save(planMapper.toJpa(plan));

        if (clienteId != null) {
            intentoManager.resetear(clienteId);
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PlanHostingResponse> listar() {
        return planMapper.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanHostingResponse consultar(Long id) {
        HostingPlan plan = planMapper.findById(id);
        if (plan == null) throw new PlanNoEncontradoException(id);
        return toResponse(plan);
    }

    public PlanHostingResponse modificar(Long id, ModificarPlanRequest request) {
        HostingPlan existente = planMapper.findById(id);
        if (existente == null) throw new PlanNoEncontradoException(id);

        String tipoPlan = extraerTipoPlan(existente);
        String plataforma = "Unix/Linux".equals(existente.getPlataforma()) ? "UNIX" : "WINDOWS";

        HostingPlan plan = crearDomain(tipoPlan, plataforma,
                id, request.nombre(), request.precioMensual(),
                request.espacioDisco(), request.anchoBanda(), request.cuentasEmail(),
                request.mysqlIncluido(), request.phpVersion(),
                request.sqlServerIncluido(), request.iisVersion(),
                request.pythonIncluido(), request.aspNetVersion());

        planMapper.update(plan);

        return toResponse(plan);
    }

    public void eliminar(Long id) {
        HostingPlan plan = planMapper.findById(id);
        if (plan == null) throw new PlanNoEncontradoException(id);
        planMapper.delete(id);
    }

    public PlanHostingResponse asociarPlataforma(Long planId, Long clienteId,
                                                  AsociarPlataformaRequest request) {
        HostingPlan existente = planMapper.findById(planId);
        if (existente == null) throw new PlanNoEncontradoException(planId);

        if (clienteId != null) {
            ResultadoIntento resultado = intentoManager.registrarIntento(clienteId);
            if (resultado.limiteExcedido()) {
                throw new IntentoLimiteExcedidoException();
            }
        }

        String tipoPlan = extraerTipoPlan(existente);

        HostingPlan plan = crearDomain(tipoPlan, request.plataforma(),
                null, existente.getNombre(), existente.getPrecioMensual(),
                existente.getEspacioDisco(), existente.getAnchoBanda(), existente.getCuentasEmail(),
                request.mysqlIncluido(), request.phpVersion(),
                request.sqlServerIncluido(), request.iisVersion(),
                request.pythonIncluido(), request.aspNetVersion());

        planMapper.insert(plan);

        if (clienteId != null) {
            intentoManager.resetear(clienteId);
        }

        return toResponse(plan);
    }

    private HostingPlan crearDomain(String tipoPlan, String plataforma,
                                     Long id, String nombre, double precioMensual,
                                     int espacioDisco, int anchoBanda, int cuentasEmail,
                                     Boolean mysqlIncluido, String phpVersion,
                                     Boolean sqlServerIncluido, String iisVersion,
                                     Boolean pythonIncluido, String aspNetVersion) {
        boolean isUnix = "UNIX".equalsIgnoreCase(plataforma);
        boolean isWindows = "WINDOWS".equalsIgnoreCase(plataforma);

        switch (tipoPlan.toUpperCase()) {
            case "PLATINO":
                if (isUnix) {
                    return new PlanPlatinoUnix(id, nombre, precioMensual, espacioDisco,
                            anchoBanda, cuentasEmail,
                            mysqlIncluido != null && mysqlIncluido, phpVersion != null ? phpVersion : "8.2");
                } else if (isWindows) {
                    return new PlanPlatinoWindows(id, nombre, precioMensual, espacioDisco,
                            anchoBanda, cuentasEmail,
                            sqlServerIncluido != null && sqlServerIncluido, iisVersion != null ? iisVersion : "10.0");
                }
                break;
            case "PLATA":
                if (isUnix) {
                    return new PlanPlataUnix(id, nombre, precioMensual, espacioDisco,
                            anchoBanda, cuentasEmail);
                } else if (isWindows) {
                    return new PlanPlataWindows(id, nombre, precioMensual, espacioDisco,
                            anchoBanda, cuentasEmail);
                }
                break;
            case "ORO":
                if (isUnix) {
                    return new PlanOroUnix(id, nombre, precioMensual, espacioDisco,
                            anchoBanda, cuentasEmail,
                            pythonIncluido != null && pythonIncluido);
                } else if (isWindows) {
                    return new PlanOroWindows(id, nombre, precioMensual, espacioDisco,
                            anchoBanda, cuentasEmail,
                            aspNetVersion != null ? aspNetVersion : "4.8");
                }
                break;
        }

        throw new IllegalArgumentException("Combinación inválida: tipoPlan=" + tipoPlan + ", plataforma=" + plataforma);
    }

    private String extraerTipoPlan(HostingPlan plan) {
        if (plan instanceof PlanPlatinoUnix || plan instanceof PlanPlatinoWindows) return "PLATINO";
        if (plan instanceof PlanPlataUnix || plan instanceof PlanPlataWindows) return "PLATA";
        if (plan instanceof PlanOroUnix || plan instanceof PlanOroWindows) return "ORO";
        throw new IllegalArgumentException("Tipo de plan desconocido: " + plan.getClass());
    }

    private PlanHostingResponse toResponse(HostingPlan plan) {
        Map<String, String> caracteristicas = plan.getCaracteristicas();
        String tipoPlan = extraerTipoPlan(plan);
        String plataforma = "UNIX".equalsIgnoreCase(plan.getPlataforma())
                ? "UNIX" : "WINDOWS".equalsIgnoreCase(plan.getPlataforma()) ? "WINDOWS" : plan.getPlataforma();

        Boolean mysqlIncluido = plan instanceof PlanPlatinoUnix p ? p.isMysqlIncluido() : null;
        String phpVersion = plan instanceof PlanPlatinoUnix p ? p.getPhpVersion() : null;
        Boolean sqlServerIncluido = plan instanceof PlanPlatinoWindows p ? p.isSqlServerIncluido() : null;
        String iisVersion = plan instanceof PlanPlatinoWindows p ? p.getIisVersion() : null;
        Boolean pythonIncluido = plan instanceof PlanOroUnix p ? p.isPythonIncluido() : null;
        String aspNetVersion = plan instanceof PlanOroWindows p ? p.getAspNetVersion() : null;

        return new PlanHostingResponse(
                plan.getId(), plan.getNombre(), plan.getPrecioMensual(),
                plan.getEspacioDisco(), plan.getAnchoBanda(), plan.getCuentasEmail(),
                tipoPlan, plataforma,
                mysqlIncluido, phpVersion,
                sqlServerIncluido, iisVersion,
                pythonIncluido, aspNetVersion,
                caracteristicas);
    }

    private PlanHostingResponse toResponse(HostingPlanJpa jpa) {
        return toResponse(planMapper.toDomain(jpa));
    }
}
