package com.chibchaweb.chibchaweb.acceso.infrastructure.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.acceso.domain.CuentaAcceso;
import com.chibchaweb.chibchaweb.shared.domain.DataMapper;

@Component
public class CuentaAccesoMapper implements DataMapper<CuentaAcceso, Long> {

    private final CuentaAccesoJpaRepository repository;
    private final RolMapper rolMapper;
    private final CredencialMapper credencialMapper;

    public CuentaAccesoMapper(CuentaAccesoJpaRepository repository,
                              RolMapper rolMapper,
                              CredencialMapper credencialMapper) {
        this.repository = repository;
        this.rolMapper = rolMapper;
        this.credencialMapper = credencialMapper;
    }

    @Override
    public void insert(CuentaAcceso entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void update(CuentaAcceso entidad) {
        repository.save(toJpa(entidad));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CuentaAcceso findById(Long id) {
        return repository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<CuentaAcceso> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    public CuentaAccesoJpa toJpa(CuentaAcceso domain) {
        if (domain == null) return null;
        CuentaAccesoJpa jpa = new CuentaAccesoJpa();
        jpa.setId(domain.getId());
        jpa.setUsuarioId(domain.getUsuarioId());
        jpa.setEstado(domain.getEstado());
        jpa.setFechaUltimoAcceso(domain.getFechaUltimoAcceso());
        jpa.setRol(rolMapper.toJpa(domain.getRol()));
        jpa.setCredencial(credencialMapper.toJpa(domain.getCredencial()));
        return jpa;
    }

    public CuentaAcceso toDomain(CuentaAccesoJpa jpa) {
        if (jpa == null) return null;
        return new CuentaAcceso(
                jpa.getId(),
                jpa.getUsuarioId(),
                rolMapper.toDomain(jpa.getRol()),
                credencialMapper.toDomain(jpa.getCredencial()));
    }
}
