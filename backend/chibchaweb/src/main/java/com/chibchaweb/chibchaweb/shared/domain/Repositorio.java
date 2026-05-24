package com.chibchaweb.chibchaweb.shared.domain;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T, ID> {

    void guardar(T entidad);

    Optional<T> buscarPorId(ID id);

    void actualizar(T entidad);

    void eliminar(ID id);

    List<T> listarTodos();
}
