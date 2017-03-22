package com.sergeev.studapp.dao;

import com.sergeev.studapp.mongoDao.MongoDaoFactory;
import com.sergeev.studapp.pgDao.PgDaoFactory;

public abstract class DaoFactory {

    public static final int POSTGRES = 1;
    public static final int MONGO = 2;

    public abstract CourseDao getCourseDao();
    public abstract DisciplineDao getDisciplineDao();
    public abstract GroupDao getGroupDao();
    public abstract LessonDao getLessonDao();
    public abstract MarkDao getMarkDao();
    public abstract UserDao getUserDao();

    public static DaoFactory getDaoFactory(int whichFactory) {
        switch (whichFactory) {
            case POSTGRES:
                return new PgDaoFactory();
            case MONGO:
                return new MongoDaoFactory();
            default:
                return new PgDaoFactory();
        }
    }
}