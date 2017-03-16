package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Identified;

import java.util.List;

public interface GenericDao<T extends Identified> {

    T persist(T object) throws PersistentException;

    T getById(Integer key) throws PersistentException;

    void update(T object) throws PersistentException;

    void delete(T object) throws PersistentException;

    void delete(Integer key) throws PersistentException;

    List<T> getAll() throws PersistentException;
}
