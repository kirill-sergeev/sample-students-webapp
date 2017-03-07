package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgCourseDao extends PgGenericDao<Course, Integer> implements CourseDao {

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
        return "INSERT INTO courses (discipline_id, group_id, teacher_id) VALUES (?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE courses SET discipline_id= ?, group_id= ?, teacher_id= ? WHERE course_id= ?;";
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
                PgTeacherDao ptd = new PgTeacherDao();
                course.setId(rs.getInt("course_id"));
                course.setGroup(pgd.getByPK(rs.getInt("group_id")));
                course.setDiscipline(pdd.getByPK(rs.getInt("discipline_id")));
                course.setTeacher(ptd.getByPK(rs.getInt("teacher_id")));
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
            statement.setInt(1, object.getDiscipline().getId());
            statement.setInt(2, object.getGroup().getId());
            statement.setInt(3, object.getTeacher().getId());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Course object) throws PersistentException {
        try {
            statement.setInt(1, object.getDiscipline().getId());
            statement.setInt(2, object.getGroup().getId());
            statement.setInt(3, object.getTeacher().getId());
            statement.setInt(4, object.getId());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Course> getByDiscipline(Integer disciplineId) throws PersistentException {
        List<Course> list;
        String sql = "SELECT * FROM courses WHERE discipline_id= ?;";
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, disciplineId);
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
    public List<Course> getByGroup(Integer groupId) throws PersistentException {
        List<Course> list;
        String sql = "SELECT * FROM courses WHERE group_id= ?;";
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
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
    public List<Course> getByTeacher(Integer teacherId) throws PersistentException {
        List<Course> list;
        String sql = "SELECT * FROM courses WHERE teacher_id= ?;";
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, teacherId);
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

}
