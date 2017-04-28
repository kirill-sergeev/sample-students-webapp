package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaDaoFactory extends DaoFactory {

    private static EntityManagerFactory entityManagerFactory;
    private static final JpaDaoFactory DAO_FACTORY = new JpaDaoFactory();

    public static JpaDaoFactory getInstance() {
        return DAO_FACTORY;
    }

    private JpaDaoFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("learn");
    }

    public static EntityManager getConnection() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public AccountDao getAccountDao() {
        return new JpaAccountDao();
    }

    @Override
    public CourseDao getCourseDao() {
        return new JpaCourseDao();
    }

    @Override
    public DisciplineDao getDisciplineDao() {
        return new JpaDisciplineDao();
    }

    @Override
    public GroupDao getGroupDao() {
        return new JpaGroupDao();
    }

    @Override
    public LessonDao getLessonDao() {
        return new JpaLessonDao();
    }

    @Override
    public MarkDao getMarkDao() {
        return new JpaMarkDao();
    }

    @Override
    public UserDao getUserDao() {
        return new JpaUserDao();
    }
}
