package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class JpaGenericDao<T extends Identified> implements GenericDao<T> {

    protected EntityTransaction transaction;
    protected Class<T> entityClass;
    protected EntityManager entityManager = JpaDaoFactory.getConnection();

    public JpaGenericDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public void save(T object) {
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(object);
            transaction.commit();
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void update(T object) {
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(object);
            transaction.commit();
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.remove(getById(id));
            transaction.commit();
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public T getById(Integer id) {
        try {
            return entityManager.find(entityClass, id);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<T> getAll() {
        try {
            String select = String.format("SELECT e FROM %s e", entityClass.getSimpleName());
            TypedQuery<T> query = entityManager.createQuery(select, entityClass);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

}
