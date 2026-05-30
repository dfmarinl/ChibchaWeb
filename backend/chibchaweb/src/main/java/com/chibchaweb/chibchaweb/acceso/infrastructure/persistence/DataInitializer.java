package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.chibchaweb.chibchaweb.acceso.domain.NombreRol;
import com.chibchaweb.chibchaweb.acceso.infrastructure.security.BcryptPasswordEncoder;

@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RolJpaRepository rolRepo;
    private final PermisoJpaRepository permisoRepo;
    private final CuentaAccesoJpaRepository cuentaRepo;
    private final CredencialJpaRepository credencialRepo;
    private final BcryptPasswordEncoder passwordEncoder;

    public DataInitializer(RolJpaRepository rolRepo,
                           PermisoJpaRepository permisoRepo,
                           CuentaAccesoJpaRepository cuentaRepo,
                           CredencialJpaRepository credencialRepo,
                           BcryptPasswordEncoder passwordEncoder) {
        this.rolRepo = rolRepo;
        this.permisoRepo = permisoRepo;
        this.cuentaRepo = cuentaRepo;
        this.credencialRepo = credencialRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        if (cuentaRepo.count() > 0) {
            log.info("Database already seeded, skipping initialization");
            return;
        }

        log.info("Seeding database with initial data...");

        PermisoJpa pUsuariosVer = crearPermiso("USUARIOS_VER");
        PermisoJpa pUsuariosCrear = crearPermiso("USUARIOS_CREAR");
        PermisoJpa pUsuariosEditar = crearPermiso("USUARIOS_EDITAR");
        PermisoJpa pUsuariosEliminar = crearPermiso("USUARIOS_ELIMINAR");
        PermisoJpa pFacturasVer = crearPermiso("FACTURAS_VER");
        PermisoJpa pFacturasCrear = crearPermiso("FACTURAS_CREAR");
        PermisoJpa pFacturasEditar = crearPermiso("FACTURAS_EDITAR");
        PermisoJpa pPedidosVer = crearPermiso("PEDIDOS_VER");
        PermisoJpa pPedidosCrear = crearPermiso("PEDIDOS_CREAR");
        PermisoJpa pProductosVer = crearPermiso("PRODUCTOS_VER");
        PermisoJpa pProductosGestionar = crearPermiso("PRODUCTOS_GESTIONAR");
        PermisoJpa pReportesVer = crearPermiso("REPORTES_VER");

        RolJpa rolAdmin = crearRol(NombreRol.ADMINISTRADOR, Set.of(
                pUsuariosVer, pUsuariosCrear, pUsuariosEditar, pUsuariosEliminar,
                pFacturasVer, pFacturasCrear, pFacturasEditar,
                pPedidosVer, pPedidosCrear,
                pProductosVer, pProductosGestionar,
                pReportesVer));

        RolJpa rolEmpleado = crearRol(NombreRol.EMPLEADO, Set.of(
                pFacturasVer, pFacturasCrear, pFacturasEditar,
                pPedidosVer, pPedidosCrear,
                pProductosVer, pProductosGestionar));

        RolJpa rolCliente = crearRol(NombreRol.CLIENTE, Set.of(
                pFacturasVer, pPedidosVer, pPedidosCrear, pProductosVer));

        CredencialJpa credencialAdmin = new CredencialJpa();
        credencialAdmin.setEmail("admin@chibchaweb.com");
        credencialAdmin.setHashContrasena(passwordEncoder.encode("admin123"));
        credencialAdmin.setIntentosFallidos(0);
        credencialRepo.save(credencialAdmin);

        CuentaAccesoJpa cuentaAdmin = new CuentaAccesoJpa();
        cuentaAdmin.setEstado(com.chibchaweb.chibchaweb.acceso.domain.EstadoCuenta.ACTIVA);
        cuentaAdmin.setRol(rolAdmin);
        cuentaAdmin.setCredencial(credencialAdmin);
        cuentaRepo.save(cuentaAdmin);

        log.info("Database seeded successfully");
        log.info("Admin account: admin@chibchaweb.com / admin123");
    }

    private PermisoJpa crearPermiso(String nombre) {
        PermisoJpa permiso = new PermisoJpa();
        permiso.setNombre(nombre);
        return permisoRepo.save(permiso);
    }

    private RolJpa crearRol(NombreRol nombre, Set<PermisoJpa> permisos) {
        RolJpa rol = new RolJpa();
        rol.setNombre(nombre);
        rol.setPermisos(permisos);
        return rolRepo.save(rol);
    }
}
