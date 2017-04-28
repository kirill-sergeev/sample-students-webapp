package com.sergeev.studapp.orm;

import com.sergeev.studapp.dao.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class OrmDaoFactory extends DaoFactory {

    private static final OrmDaoFactory DAO_FACTORY = new OrmDaoFactory();
    private static EntityManagerFactory entityManagerFactory;

    private OrmDaoFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("learn");
    }

    public static EntityManager getConnection() {
        return entityManagerFactory.createEntityManager();
    }

    public static OrmDaoFactory getInstance(){
        return DAO_FACTORY;
    }

    @Override
    public AccountDao getAccountDao() {
        return null;
    }

    @Override
    public CourseDao getCourseDao() {
        return null;
    }

    @Override
    public DisciplineDao getDisciplineDao() {
        return null;
    }

    @Override
    public GroupDao getGroupDao() {
        return new OrmGroupDao();
    }

    @Override
    public LessonDao getLessonDao() {
        return null;
    }

    @Override
    public MarkDao getMarkDao() {
        return null;
    }

    @Override
    public UserDao getUserDao() {
        return null;
    }
}
