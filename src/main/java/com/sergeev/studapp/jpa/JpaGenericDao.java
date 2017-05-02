package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.GenericDao;
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
    public void save(T object) {
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(object);
        transaction.commit();
    }

    @Override
    public void update(T object) {
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(object);
        transaction.commit();
    }

    @Override
    public void remove(Integer id) {
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(getById(id));
        transaction.commit();
    }

    @Override
    public T getById(Integer id) {
        return entityManager.find(entityClass, id);
    }
    
    @Override
    public List<T> getAll() {
        String select = String.format("SELECT e FROM %s e", entityClass.getSimpleName());
        TypedQuery<T> query = entityManager.createQuery(select, entityClass);
        return  query.getResultList();
    }
    
}
