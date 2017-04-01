package com.sergeev.studapp.dao;

import com.sergeev.studapp.mongo.MongoDaoFactory;
import com.sergeev.studapp.postgres.PgDaoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DaoFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DaoFactory.class);

    public static final int POSTGRES = 1;
    public static final int MONGO = 2;

    public abstract AccountDao getAccountDao();
    public abstract CourseDao getCourseDao();
    public abstract DisciplineDao getDisciplineDao();
    public abstract GroupDao getGroupDao();
    public abstract LessonDao getLessonDao();
    public abstract MarkDao getMarkDao();
    public abstract UserDao getUserDao();

    public static DaoFactory getDaoFactory(int whichFactory) {
        switch (whichFactory) {
            case POSTGRES:
                return MongoDaoFactory.getInstance();
            case MONGO:
                return PgDaoFactory.getInstance();
            default:
                throw new IllegalArgumentException();
        }
    }
}