package com.mozcalti.gamingapp.commons;

import java.io.Serializable;
import java.util.List;

public interface GenericServiceAPI<T, K extends Serializable> {

    T save(T entity);

    void delete(K id);

    T get(K id);

    List<T> getAll();

}