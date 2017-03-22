package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.postgres.PgDisciplineDao.DISCIPLINE_ID;
import static com.sergeev.studapp.postgres.PgGroupDao.GROUP_ID;
import static com.sergeev.studapp.postgres.PgUserDao.USER_ID;

public class PgCourseDao extends PgGenericDao<Course> implements CourseDao {

    protected static final String COURSE_ID = "course_id";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM courses WHERE course_id= ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM courses;";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO courses (discipline_id, group_id, user_id) VALUES (?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE courses SET discipline_id= ?, group_id= ?, user_id= ? WHERE course_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM courses WHERE course_id= ?;";
    }

    @Override
    protected List<Course> parseResultSet(ResultSet rs) throws PersistentException {
        List<Course> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Course course = new Course();
                PgDisciplineDao pdd = new PgDisciplineDao();
                PgGroupDao pgd = new PgGroupDao();
                PgUserDao ptd = new PgUserDao();
                course.setId(rs.getString(COURSE_ID));
                course.setGroup(pgd.getById(rs.getString(GROUP_ID)));
                course.setDiscipline(pdd.getById(rs.getString(DISCIPLINE_ID)));
                course.setTeacher(ptd.getById(rs.getString(USER_ID)));
                result.add(course);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Course object) throws PersistentException {
        try {
            statement.setInt(1, Integer.parseInt(object.getDiscipline().getId()));
            statement.setInt(2, Integer.parseInt(object.getGroup().getId()));
            statement.setInt(3, Integer.parseInt(object.getTeacher().getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Course object) throws PersistentException {
        try {
            statement.setInt(1, Integer.parseInt(object.getDiscipline().getId()));
            statement.setInt(2, Integer.parseInt(object.getGroup().getId()));
            statement.setInt(3, Integer.parseInt(object.getTeacher().getId()));
            statement.setInt(4, Integer.parseInt(object.getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Course> getByDiscipline(String disciplineId) throws PersistentException {
        List<Course> list;
        String sql = "SELECT * FROM courses WHERE discipline_id= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(disciplineId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<Course> getByGroup(String groupId) throws PersistentException {
        List<Course> list;
        String sql = "SELECT * FROM courses WHERE group_id= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(groupId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<Course> getByTeacher(String userId) throws PersistentException {
        List<Course> list;
        String sql = "SELECT * FROM courses WHERE user_id= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(userId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public Course getByGroupAndDiscipline(String groupId, String disciplineId) throws PersistentException {
        List<Course> list;
        String sql = "SELECT * FROM courses WHERE group_id= ? AND discipline_id= ?";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(groupId));
            statement.setInt(2, Integer.parseInt(disciplineId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }

}
