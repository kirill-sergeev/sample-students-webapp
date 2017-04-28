package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class PgDaoFactory extends DaoFactory {

    private static final PgDaoFactory PG_DAO_FACTORY = new PgDaoFactory();
    private static DataSource dataSource;
    static int count = 0;
    static{
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/postgresql");
        } catch (NamingException e) {
            //Off while dev.
            //throw new IllegalStateException("Missing JNDI!", e);
        }
    }

    private PgDaoFactory(){
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

    public static PgDaoFactory getInstance(){
        return PG_DAO_FACTORY;
    }

    @Override
    public AccountDao getAccountDao() {
        return new PgAccountDao();
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
    public UserDao getUserDao() {
        return new PgUserDao();
    }

}
