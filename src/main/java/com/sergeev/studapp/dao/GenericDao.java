package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Identified;

import java.util.List;

public interface GenericDao<T extends Identified<PK>, PK extends Integer> {

    T persist(T object) throws PersistentException;

    T getByPK(PK key) throws PersistentException;

    void update(T object) throws PersistentException;

    void delete(T object) throws PersistentException;

    void delete(PK key) throws PersistentException;

    List<T> getAll() throws PersistentException;
}
