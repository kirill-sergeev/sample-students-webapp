package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaDaoFactory extends DaoFactory {

    private static final JpaDaoFactory DAO_FACTORY = new JpaDaoFactory();
    private static EntityManagerFactory entityManagerFactory;

    public static JpaDaoFactory getInstance() {
        return DAO_FACTORY;
    }

    public static EntityManager getConnection() {
        return entityManagerFactory.createEntityManager();
    }

    private JpaDaoFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("learn");
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
