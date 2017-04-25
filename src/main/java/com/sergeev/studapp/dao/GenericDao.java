package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Identified;

import java.util.List;

public interface GenericDao<T extends Identified> {

    T save(T object) throws PersistentException;

    T getById(Integer id) throws PersistentException;

    T update(T object) throws PersistentException;

    void delete(Integer id) throws PersistentException;

    List<T> getAll() throws PersistentException;
}
