package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgDaoFactory extends DaoFactory {

    private static final String DRIVER = "org.postgresql.Driver";
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
//  for ElephantSQL
//        URL="jdbc:postgresql://echo-01.db.elephantsql.com:5432/dcowipjd";
//        USER="dcowipjd";
//        PASSWORD="BAbsnENkcmB2v7FLLHpxYc1o0tCPKAJR";
//  for PostgreSQL at Bluemix
//        URL = "jdbc:postgresql://159.8.128.91:5433/d6dff8b6212054249bf05aed791106499";
//        USER = "ue8d9771e79e840de8f1a3bb47f05ca96";
//        PASSWORD = "pdbdf01712fe54ada8739a3c7041d1182";
//  for PostgresQL at localhost
        URL = "jdbc:postgresql://localhost:5432/postgres_test";
        USER = "root";
        PASSWORD = "root";
    }

    static Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PgDaoFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CourseDao getCourseDao() {
        return new PgCourseDao();
    }

    @Override
    public DisciplineDao getDisciplineDao() {
        return new PgDisciplineDao();
    }

    @Override
    public GroupDao getGroupDao() {
        return new PgGroupDao();
    }

    @Override
    public LessonDao getLessonDao() {
        return new PgLessonDao();
    }

    @Override
    public MarkDao getMarkDao() {
        return new PgMarkDao();
    }

    @Override
    public StudentDao getStudentDao() {
        return new PgStudentDao();
    }

    @Override
    public TeacherDao getTeacherDao() {
        return new PgTeacherDao();
    }
}
