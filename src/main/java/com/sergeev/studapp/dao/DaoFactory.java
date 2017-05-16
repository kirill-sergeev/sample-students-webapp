package com.sergeev.studapp.dao;

import com.sergeev.studapp.jpa.JpaDaoFactory;
import com.sergeev.studapp.mongo.MongoDaoFactory;
import com.sergeev.studapp.postgres.PgDaoFactory;

public abstract class DaoFactory implements AutoCloseable{

    public static final int POSTGRESQL = 1;
    public static final int MONGODB = 2;
    public static final int JPA = 3;

    public static DaoFactory getDaoFactory(int whichFactory) {
        switch (whichFactory) {
            case POSTGRESQL:
                return new PgDaoFactory();
            case MONGODB:
                return MongoDaoFactory.getInstance();
            case JPA:
                return JpaDaoFactory.getInstance();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static DaoFactory getDaoFactory() {
        return getDaoFactory(JPA);
    }

    public abstract void startTransaction();
    public abstract void abortTransaction();

    public abstract CourseDao getCourseDao();
    public abstract DisciplineDao getDisciplineDao();
    public abstract GroupDao getGroupDao();
    public abstract LessonDao getLessonDao();
    public abstract MarkDao getMarkDao();
    public abstract UserDao getUserDao();

}