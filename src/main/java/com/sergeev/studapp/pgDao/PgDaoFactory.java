package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgDaoFactory extends DaoFactory {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres_test";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
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
