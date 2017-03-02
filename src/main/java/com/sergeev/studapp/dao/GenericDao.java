package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Identified;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T extends Identified<PK>, PK extends Serializable> {

    void persist(T object) throws PersistException;

    T getByPK(PK key) throws PersistException;

    void update(T object) throws PersistException;

    void delete(T object) throws PersistException;

    List<T> getAll() throws PersistException;
}
