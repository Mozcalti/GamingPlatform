package com.mozcalti.gamingapp.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class GenericServiceImpl<T, K extends Serializable> implements GenericServiceAPI<T, K> {

    @Override
    public T save(T entity) {
        return getDao().save(entity);
    }

    @Override
    public void delete(K id) {
        getDao().deleteById(id);
    }

    @Override
    public T get(K id) {
        Optional<T> obj = getDao().findById(id);
        if (obj.isPresent()) {
            return obj.get();
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        List<T> returnList = new ArrayList<>();
        getDao().findAll().forEach(returnList::add);
        return returnList;
    }

    public abstract CrudRepository<T, K> getDao();

}

