package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class JpaGenericDao <T extends Identified> implements GenericDao<T> {

    protected EntityTransaction transaction;
    protected Class<T> entityClass;
    protected EntityManager entityManager = JpaDaoFactory.getConnection();

    public JpaGenericDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public T save(T object) throws PersistentException {
        transaction = entityManager.getTransaction();
        transaction.begin();
        System.out.println(object);
        entityManager.persist(object);
        transaction.commit();
        return object;
    }

    @Override
    public T getById(Integer id) throws PersistentException {
        return entityManager.find(entityClass, id);
    }

    @Override
    public T update(T object) throws PersistentException {
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(object);
        transaction.commit();
        return object;
    }

    @Override
    public void delete(Integer id) throws PersistentException {
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(getById(id));
        transaction.commit();
    }

    @Override
    public List<T> getAll() throws PersistentException {
        String select = String.format("SELECT e FROM %s e", entityClass.getSimpleName());
        TypedQuery<T> query = entityManager.createQuery(select, entityClass);
        return  query.getResultList();
    }
}
