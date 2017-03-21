package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.*;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class PgDaoFactory extends DaoFactory {

    private static final String DRIVER;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    private static final String PROPERTIES_FILE = "db_postgres_local.properties";
    private static final Properties PROPERTIES = new Properties();

    private static BasicDataSource dataSource;

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream resourceStream = classLoader.getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DRIVER = PROPERTIES.getProperty("driver");
        URL = PROPERTIES.getProperty("url");
        USER = PROPERTIES.getProperty("username");
        PASSWORD = PROPERTIES.getProperty("password");
    }

    private static BasicDataSource getDataSource() {
        if (dataSource == null) {
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl(URL);
            ds.setUsername(USER);
            ds.setPassword(PASSWORD);
            ds.setDriverClassName(DRIVER);

            ds.setMinIdle(5);
            ds.setMaxIdle(10);
            ds.setMaxOpenPreparedStatements(5);

            dataSource = ds;
        }
        return dataSource;
    }

    static Connection getConnection() {
        Connection connection = null;
        BasicDataSource dataSource = PgDaoFactory.getDataSource();
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
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
