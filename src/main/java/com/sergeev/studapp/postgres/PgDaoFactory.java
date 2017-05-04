package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class PgDaoFactory extends DaoFactory {

    private static DataSource dataSource;
    private Connection connection;

    static int count = 0;
    static{
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/postgresql");
        } catch (NamingException e) {
            //Off while dev.
            //throw new IllegalStateException("Missing JNDI!", e);
        }
    }

    public PgDaoFactory(){
        connection = getConnection();
    }

    public static Connection getConnection() {
        Connection connection = null;
        try{
            /////Only for development!///////
            if (dataSource == null){
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/students?user=root&password=root");
                return connection;
            }
            ////////////////////////////////
            connection = dataSource.getConnection();
            System.out.println(++count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void startTransaction() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void abortTransaction() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CourseDao getCourseDao() {
        return new PgCourseDao(connection);
    }
    @Override
    public DisciplineDao getDisciplineDao() {
        return new PgDisciplineDao(connection);
    }
    @Override
    public GroupDao getGroupDao() {
        return new PgGroupDao(connection);
    }
    @Override
    public LessonDao getLessonDao() {
        return new PgLessonDao(connection);
    }
    @Override
    public MarkDao getMarkDao() {
        return new PgMarkDao(connection);
    }
    @Override
    public UserDao getUserDao() {
        return new PgUserDao(connection);
    }

}
