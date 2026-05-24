package com.chibchaweb.chibchaweb.shared.domain;

import java.util.List;

public interface DataMapper<T, ID> {

    void insert(T entidad);

    void update(T entidad);

    void delete(ID id);

    T findById(ID id);

    List<T> findAll();
}
