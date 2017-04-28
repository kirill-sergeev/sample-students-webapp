package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Identified;

import java.util.List;

public interface GenericDao<T extends Identified> {

    T getById(Integer id);

    void remove(Integer id);

    void save(T object);

    void update(T object);

    List<T> getAll();

}
