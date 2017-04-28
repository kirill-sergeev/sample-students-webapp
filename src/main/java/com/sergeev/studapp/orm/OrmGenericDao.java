package com.sergeev.studapp.orm;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;

import java.util.List;

public abstract class OrmGenericDao<T extends Identified> implements GenericDao<T> {


    @Override
    public T save(T object) throws PersistentException {
        return null;
    }

    @Override
    public T getById(Integer id) throws PersistentException {
        return null;
    }

    @Override
    public T update(T object) throws PersistentException {
        return null;
    }

    @Override
    public void delete(Integer id) throws PersistentException {

    }

    @Override
    public List<T> getAll() throws PersistentException {
        return null;
    }
}
